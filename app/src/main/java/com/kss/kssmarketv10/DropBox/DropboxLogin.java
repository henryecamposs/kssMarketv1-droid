package com.kss.kssmarketv10.DropBox;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;
import com.kss.kssmarketv10.DropBox.DbxClient.DropboxClientFactory;
import com.kss.kssmarketv10.DropBox.DbxClient.PicassoClient;
import com.kss.kssmarketv10.DropBox.adapters.FilesAdapters;
import com.kss.kssmarketv10.DropBox.asynctasks.DownloadFileTask;
import com.kss.kssmarketv10.DropBox.asynctasks.GetFilesDetailsTask;
import com.kss.kssmarketv10.DropBox.asynctasks.UploadFileTasks;
import com.kss.kssmarketv10.DropBox.interfaces.DownloadFileCallback;
import com.kss.kssmarketv10.DropBox.interfaces.FilesCallback;
import com.kss.kssmarketv10.DropBox.interfaces.FilesDetailCallback;
import com.kss.kssmarketv10.DropBox.interfaces.UploadFileCallback;
import com.kss.kssmarketv10.R;
import com.kss.kssmarketv10.kssSettings;
import com.kss.kssmarketv10.swipe.util.Attributes;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.clsUtil_Files;
import com.kss.kssutil.enuToastIcons;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

public class DropboxLogin extends AppCompatActivity {
    private static final String TAG = DropboxLogin.class.getName();
    private static final int PICKFILE_REQUEST_CODE = 1;
    private String ACCESS_TOKEN;
    private TextView tvLogin_Log;
    private TextView tvBackBD_Log;
    public kssSettings setting;
    private LinearLayout llListFiles_Log;
    private ListView lvFilesDownload_log;

    public final static String EXTRA_PATH = "DropboxLogin_Path";
    private String mPath;
    private FilesAdapters mFilesAdapter;
    private FileMetadata mSelectedFile;

    public static Intent getIntent(Context context, String path) {
        Intent filesIntent = new Intent(context, DropboxLogin.class);
        filesIntent.putExtra(DropboxLogin.EXTRA_PATH, path);
        return filesIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dropbox);
        setting = kssSettings.getInstance(this);
        tvLogin_Log = (TextView) findViewById(R.id.tvLogin_Log);
        tvBackBD_Log = (TextView) findViewById(R.id.tvBackBD_Log);
        llListFiles_Log = (LinearLayout) findViewById(R.id.llListFiles_Log);
        lvFilesDownload_log = (ListView) findViewById(R.id.lvFilesDownload_log);
        final ImageView ivexit = (ImageView) findViewById(R.id.ivExit_Log);
        final ImageView ibHome_Log = (ImageView) findViewById(R.id.ibHome_Log);
        ivexit.setOnClickListener(OnClickListener);
        ibHome_Log.setOnClickListener(OnClickListener);

        tvLogin_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!tokenExists())
                        //Conectar
                        Auth.startOAuth2Authentication(view.getContext(), setting.DropBox_APP_KEY);
                    else {
                        //Desconectar
                        //todo Revisar
                        setting.setDropBox_AuthToken(null);
                    }
                    Logged(tokenExists());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Excepcion: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        tvBackBD_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Respaldo Local
                clsUtil_Files.BDbackup_return Backup_result = null;
                try {
                    Backup_result = clsUtil_Files.BD_backup(v.getContext(), setting.getNombreBD(), setting.getRutaBackupBD(), setting.getRutaBD());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Backup_result.getResult())
                    ToastManager.show(v.getContext(), "Base de datos respaldada correctamente!\nBase de datos" + Backup_result.getPathArchivo_result(), enuToastIcons.OK, 10);
                else
                    ToastManager.show(v.getContext(),
                            String.format("Datos NO GUARDADOS:\n%s\n%s", setting.getRutaBD(), Backup_result.getPathArchivo_result())
                            , enuToastIcons.WARNING, 20);
                //Subir archivo
                File file = new File(setting.getRutaBD(), setting.getNombreBD());
                if (file != null) {
                    if (file.exists())
                        //Initialize UploadTask
                        uploadFile(file.getAbsolutePath());
                }
            }
        });

        Logged(tokenExists());

        String path = getIntent().getStringExtra(EXTRA_PATH);
        mPath = path == null ? "" : path;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_files_data);
        mFilesAdapter = new FilesAdapters(PicassoClient.getPicasso(), new FilesCallback() {
            @Override
            public void onFolderClicked(FolderMetadata folder) {
                startActivity(DropboxLogin.getIntent(DropboxLogin.this, folder.getPathLower()));
            }

            @Override
            public void onFileClicked(final FileMetadata file) {
                mSelectedFile = file;
                performWithPermissions(DropboxLogin.FileAction.DOWNLOAD);
            }

            @Override
            public void onRenameFile(String fromName) {

            }

            @Override
            public void onDeleteFile(String filePath) {

            }

            @Override
            public void onShareFile(String filePath) {

            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFilesAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mFilesAdapter);

        mSelectedFile = null;

        loadData();

    }

    private void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    private void viewFileInExternalApp(File result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = result.getName().substring(result.getName().indexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);

        intent.setDataAndType(Uri.fromFile(result), type);

        // Check for a handler first to avoid a crash
        PackageManager manager = getPackageManager();
        List<ResolveInfo> resolveInfo = manager.queryIntentActivities(intent, 0);
        if (resolveInfo.size() > 0) {
            startActivity(intent);
        }
    }

    private boolean hasPermissionsForAction(DropboxLogin.FileAction action) {
        for (String permission : action.getPermissions()) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void performAction(DropboxLogin.FileAction action) {
        switch (action) {
            case UPLOAD:
                launchFilePicker();
                break;
            case DOWNLOAD:
                if (mSelectedFile != null) {
                    downloadFile(mSelectedFile);
                } else {
                    Log.e(TAG, "No file selected to download.");
                }
                break;
            default:
                Log.e(TAG, "Can't perform unhandled file action: " + action);
        }
    }

    private boolean shouldDisplayRationaleForAction(DropboxLogin.FileAction action) {
        for (String permission : action.getPermissions()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    private void requestPermissionsForAction(DropboxLogin.FileAction action) {
        ActivityCompat.requestPermissions(
                this,
                action.getPermissions(),
                action.getCode()
        );
    }

    private void performWithPermissions(final DropboxLogin.FileAction action) {
        if (hasPermissionsForAction(action)) {
            performAction(action);
            return;
        }

        if (shouldDisplayRationaleForAction(action)) {
            new AlertDialog.Builder(this)
                    .setMessage("This app requires storage access to download and upload files.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissionsForAction(action);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissionsForAction(action);
        }
    }

    private enum FileAction {
        DOWNLOAD(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        UPLOAD(Manifest.permission.READ_EXTERNAL_STORAGE);

        private static final DropboxLogin.FileAction[] values = values();

        private final String[] permissions;

        FileAction(String... permissions) {
            this.permissions = permissions;
        }

        public int getCode() {
            return ordinal();
        }

        public String[] getPermissions() {
            return permissions;
        }

        public static DropboxLogin.FileAction fromCode(int code) {
            if (code < 0 || code >= values.length) {
                throw new IllegalArgumentException("Invalid FileAction code: " + code);
            }
            return values[code];
        }
    }

    protected void loadData() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");
        dialog.show();

        new GetFilesDetailsTask(DropboxClientFactory.getClient(), new FilesDetailCallback() {
            @Override
            public void onFileDetailsSeccess(ListFolderResult result) {
                dialog.dismiss();
                mFilesAdapter.setFiles(result.getEntries());
            }

            @Override
            public void onFileDetailsError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to list folder.", e);
                Toast.makeText(DropboxLogin.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(mPath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
        Logged(tokenExists());
    }

    private void Logged(Boolean esLogged) {
        if (esLogged) {
            //Existe Token
            getUserAccount();
            ACCESS_TOKEN = retrieveAccessToken();
            tvLogin_Log.setText("Cerrar Sesi�n");
            tvBackBD_Log.setEnabled(true);
            llListFiles_Log.setVisibility(View.VISIBLE);
        } else {
            //No existe Token
            llListFiles_Log.setVisibility(View.INVISIBLE);
            tvBackBD_Log.setEnabled(false);
            tvLogin_Log.setText("Iniciar Sesi�n");
        }
    }

    private void downloadFile(FileMetadata file) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Downloading File...!");
        dialog.show();

        new DownloadFileTask(DropboxClientFactory.getClient(), new DownloadFileCallback() {
            @Override
            public void onDownloadComplete(File result) {
                dialog.dismiss();
                if (result != null) {
                    viewFileInExternalApp(result);
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to download file.", e);
                Toast.makeText(DropboxLogin.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(file);

    }

    private void uploadFile(String fileUri) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading File...!");
        dialog.show();

        new UploadFileTasks(DropboxClientFactory.getClient(), new UploadFileCallback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                dialog.dismiss();

                String message = result.getName() + " size " + result.getSize() + " modified " +
                        DateFormat.getDateTimeInstance().format(result.getClientModified());
                Toast.makeText(DropboxLogin.this, message, Toast.LENGTH_SHORT)
                        .show();
                mFilesAdapter.addUploadedFile(result);

            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.", e);
                Toast.makeText(DropboxLogin.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri, mPath);
    }

    private void upload() {
        if (ACCESS_TOKEN == null) return;
        //Subir archivo
        File file = new File(setting.getRutaBD(), setting.getNombreBD());
        if (file != null) {
            if (file.exists())
                //Initialize UploadTask
                new UploadTask(DropboxClient.getClient(ACCESS_TOKEN), file, this, "/" + setting.getEmpresaNombre().trim() + "/").execute();
        }
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token(); //generate Access Token
        if (accessToken != null) {
            //Store accessToken in SharedPreferences
            setting.setDropBox_AuthToken(accessToken);
            setting.guardarCambios();
            //Acceso Permitido
            Log.i(getLocalClassName().toString(), "Token:" + setting.getDropBox_AuthToken());

        }
    }

    protected void getUserAccount() {
        if (ACCESS_TOKEN == null) return;
        new DropBoxUserAccountTask(DropboxClient.getClient(ACCESS_TOKEN), new DropBoxUserAccountTask.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                Log.d("User data", account.getEmail());
                Log.d("User data", account.getName().getDisplayName());
                Log.d("User data", account.getAccountType().name());
                updateUI(account);
            }

            @Override
            public void onError(Exception error) {
                Log.d("User data", "Error receiving account details.");
            }
        }).execute();
    }

    private boolean tokenExists() {
        String accessToken = setting.getDropBox_AuthToken();
        if (accessToken != null)
            accessToken = accessToken.length() == 0 ? null : accessToken;
        return accessToken != null;
    }


    private String retrieveAccessToken() {
        //check if ACCESS_TOKEN is previously stored on previous app launches
        String accessToken = setting.getDropBox_AuthToken();
        if (accessToken == null) {
            Log.d("AccessToken Status", "No token found");
            return null;
        } else {
            //accessToken already exists
            Log.d("AccessToken Status", "Token exists");
            return accessToken;
        }
    }


    private void updateUI(FullAccount account) {
        setting.setDropBoxEmail(account.getEmail());
        setting.setDropBoxUser(account.getName().getDisplayName());
       /*Picasso.with(this)
                .load(account.getProfilePhotoUrl())
                .resize(200, 200)
                .into(profile);*/
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivExit_Log:
                    finish();
                    break;
                case R.id.ibHome_Log:
                    finish();
                    break;

            }
        }
    };
}
