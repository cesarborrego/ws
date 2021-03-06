package c.neo.boa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import c.neo.boa.lecturanfc.LecturaTag;
import c.neo.boa.utils.CheckInternetConnection;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity{
	public static final String TAG = MainActivity.class.getSimpleName();

	MainActivity ma;
	NfcAdapter nfcAdapter = null;
	PendingIntent pendingIntent = null;
	IntentFilter[] filters = null;
	String[][] techList = null;
	protected String[] datosTag;

	TextView bienvenidaTxt;
	private TextView tvUI;
	private ImageView ivUI;
	private RelativeLayout rlUI;
	LinearLayout datosNFC, vencimientoLinearLayOut;
	MenuItem search_itemUI;
	private Menu menu_mainUI;

	private String no_cedula = "";
	private boolean datosValidos = false;

	Intent lecturaIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Obtenemos el control sobre el lector de NFC
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		// Si no se encuentra el lector de NFC se cierra aplicacion
		if (nfcAdapter == null) {
			Toast.makeText(this, getString(R.string.nfc_warning),
					Toast.LENGTH_LONG).show();
			finish();
			return;

		}

		// Creamos un Intent para manejar los datos leidos
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// Creamos un filtro de Intent relacionado con descubrir un mensaje NDEF
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter discovery = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);

		// Configuramos el filtro para que acepte de cualquier tipo de NDEF
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		filters = new IntentFilter[] { ndef, discovery };

		// Configuramos para que lea de cualquier clase de tag NFC
		techList = new String[][] { new String[] { NfcF.class.getName() } };

		Intent lecturaIntent = getIntent();
		Log.d(TAG, "INTENT " + lecturaIntent.toString());
		Log.d(TAG, "PENDING INTENT " + pendingIntent.toString());
		Parcelable[] rawMsgs = lecturaIntent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		Log.d(TAG, "RAW " + rawMsgs);

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(lecturaIntent.getAction())) {
			lecturaNFC(lecturaIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (datosValidos) {
			menu.findItem(R.id.buscar_item).setIcon(R.drawable.search_wh);
		} else {
			menu.findItem(R.id.buscar_item).setIcon(R.drawable.search_na);
		}

		menu_mainUI = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.buscar_item:
			Log.d(TAG, "Click en Buscar");
			if (datosValidos) {
				Log.d(TAG, "Consultar informacion");
				if (CheckInternetConnection.isConnectedToInternet(getApplicationContext())) {
					openData();
				}else {
					Toast.makeText(getApplicationContext(), "No hay servicio de Internet", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume()");
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters,
				techList);
		lecturaNFC(lecturaIntent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause()");
		nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent()");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		lecturaNFC(intent);
	}

	public void lecturaNFC(Intent intent) {
		try {

			LecturaTag lecturaObj = new LecturaTag(intent);
			datosTag = lecturaObj.lectura();

			LinearLayout l = (LinearLayout) findViewById(R.id.bienvenida_msg);
			l.setVisibility(View.GONE);

			vencimientoLinearLayOut = (LinearLayout) findViewById(R.id.vencimientoLayOutID);
			vencimientoLinearLayOut.setVisibility(View.VISIBLE);

			datosNFC = (LinearLayout) findViewById(R.id.datosNFClLayOutID);

			if (datosTag != null) {

				Log.d(TAG, "# Datos leidos: " + datosTag.length);
				int i = 0;
				while (i < datosTag.length) {
					Log.d(TAG, "Dato[" + i + "]: " + datosTag[i]);
					i++;
				}

				tvUI = (TextView) findViewById(R.id.estado);
				ivUI = (ImageView) findViewById(R.id.estado_img);

				try {
					Calendar c = Calendar.getInstance();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"DD-MM-yyyy", Locale.getDefault());
					SimpleDateFormat outFormat = new SimpleDateFormat(
							"DD/MM/yyyy", Locale.getDefault());
					Date date_vencimiento = dateFormat.parse(datosTag[3]);

					Date hoy = c.getTime();
					datosTag[2] = datosTag[2].replace("-", "/");
					datosTag[3] = datosTag[3].replace("-", "/");

					if (hoy.before(date_vencimiento)) {
						Log.d(TAG,
								"Permiso OK: "
										+ outFormat.format(date_vencimiento));
						tvUI.setText(R.string.status_ok);
						ivUI.setImageResource(R.drawable.ok);
					} else {
						Log.d(TAG,
								"Permiso Vencido: "
										+ outFormat.format(date_vencimiento));
						tvUI.setText(R.string.status_expired);
						ivUI.setImageResource(R.drawable.vencido);
					}

				} catch (Exception e) {
					Log.e(TAG,
							"Exception::lecturaNFC:Bloque fecha->"
									+ e.getMessage());
				}

				no_cedula = datosTag[0];
//				tvUI = (TextView) findViewById(R.id.cedula);
//				tvUI.setText(no_cedula);

				tvUI = (TextView) findViewById(R.id.matricula);
				tvUI.setText(datosTag[0]);

//				tvUI = (TextView) findViewById(R.id.serie);
//				tvUI.setText(datosTag[2]);
//				tvUI = (TextView) findViewById(R.id.seccion);
//				tvUI.setText(datosTag[3]);
				
				tvUI = (TextView) findViewById(R.id.fecha_emision);
				tvUI.setText(datosTag[2]);
				tvUI = (TextView) findViewById(R.id.fecha_vencimiento);
				tvUI.setText(datosTag[3]);

				int indice_depto = Integer.parseInt(datosTag[4]);

				Resources res = getResources();
				String[] departamentos = null;
				departamentos = res.getStringArray(R.array.departamentos);

				tvUI = (TextView) findViewById(R.id.lugar_emision);
				tvUI.setText(departamentos[indice_depto]);

				// int id_img_depto =
				// getResources().getIdentifier("depto_"+datosTag[6],
				// "drawable", getPackageName());
				int id_img_depto = getResources().getIdentifier("esfera",
						"drawable", getPackageName());

				ivUI = (ImageView) findViewById(R.id.img_depto);
				ivUI.setImageResource(id_img_depto);

				try {
					search_itemUI = menu_mainUI.findItem(R.id.buscar_item);
					search_itemUI.setIcon(R.drawable.search);
				} catch (NullPointerException e) {
					Log.e(TAG, "No hay menu aun: " + e.getMessage());
				}

				datosValidos = true;
				datosNFC.setVisibility(View.VISIBLE);

			} else {

				Log.i(TAG, "Lectura de datos no validos");

				ivUI = (ImageView) findViewById(R.id.estado_img);
				ivUI.setImageResource(R.drawable.error);

				tvUI = (TextView) findViewById(R.id.estado);
				tvUI.setText(R.string.status_invalid);

				try {
					search_itemUI = menu_mainUI.findItem(R.id.buscar_item);
					search_itemUI.setIcon(R.drawable.search_na);
				} catch (NullPointerException e) {
					Log.e(TAG, "No hay menu aun: " + e.getMessage());
					e.printStackTrace();
				}

				datosValidos = false;
				rlUI.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			Log.e(TAG, "Excepion::lecturaNFC->" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void openData() {
		Intent intent = new Intent(this, DataActivity.class);
		intent.putExtra("NO_CEDULA", no_cedula);
		startActivity(intent);
	}
}
