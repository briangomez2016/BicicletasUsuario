package com.example.brian.bicicletasusuario;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class QR extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);

		try {

			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

			startActivityForResult(intent, 0);

		} catch (Exception e) {

			Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			startActivity(marketIntent);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
			}
			if(resultCode == RESULT_CANCELED){
				Toast.makeText(this, "NOPE", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
