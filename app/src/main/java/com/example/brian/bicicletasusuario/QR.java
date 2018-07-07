package com.example.brian.bicicletasusuario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;

import com.google.zxing.Result;

import butterknife.BindView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR extends AppCompatActivity implements ZXingScannerView.ResultHandler{

	private ZXingScannerView scannerView;

	@Override
	@RequiresApi(api = Build.VERSION_CODES.M)
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scannerView = new ZXingScannerView(this);
		setContentView(scannerView);
		scannerView.setResultHandler(this);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			scannerView.startCamera();
		} else {
			requestPermissions(
					new String[]{
							Manifest.permission.CAMERA
					},
					7368
			);

		}
		}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 7368) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
				return;
			}
			scannerView.startCamera();
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		scannerView.stopCamera();
	}

	@Override
	public void handleResult(Result result) {
		Intent intent = new Intent();
		intent.setData(Uri.parse(result.getText()));
		setResult(RESULT_OK,intent);
		finish();
		scannerView.resumeCameraPreview(this);

	}

}