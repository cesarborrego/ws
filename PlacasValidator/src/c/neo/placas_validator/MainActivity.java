package c.neo.placas_validator;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends Activity {
	NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	//private int mCount = 0;
	private static final String TAG = MainActivity.class.getSimpleName();
	private TextView mText;
	private ImageView imageType;
	private Button botonMain;
	private String result = null;
	private String placaNo = null;
	private int indice_entidad = 0;
	protected int idImagenEntidad = 0;
	private String type = "";
	private String imagenEntidad = "";
	private int indice_tipotransporte = 0;
	private int indice_color = 0;
	private String valid = "";

	// Declaracion de variables para consumir el web service
	private SoapObject request = null;
	private SoapSerializationEnvelope envelope = null;
	private SoapPrimitive resultsRequestSOAP = null;

	private static final String SOAP_ACTION = "";
	private static final String METHOD_NAME = "getTagNFCBalance";
	private static final String NAMESPACE = "http://impl.neology.com/";
	private static final String URL = "http://148.245.107.245:8095/WSConexionDB/services/transactiondetailservice?wsdl";

	// Declaracion de variables para serealziar y deserealizar
	// objetos y cadenas JSON
	Gson gson;
	private String strJSON;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Creacion del mensaje de espera
		dialog = new ProgressDialog(this);
		dialog.setMessage("Espere un momento");
		dialog.setTitle("Obteniendo informacion...");
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.mytitle);

		// Toast.makeText(this, "Toas TEST", Toast.LENGTH_LONG).show();

		// Obtenemos el control sobre el lector de NFC
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		// Si no se encuentra el lector de NFC se cierra aplicacion
		if (mAdapter == null) {
			Toast.makeText(this, getString(R.string.nfc_warning),
					Toast.LENGTH_LONG).show();
			finish();
			return;

		}
		// Creamos un Intent para manejar los datos leidos
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
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
		mFilters = new IntentFilter[] { ndef, discovery };

		// Configuramos para que lea de cualquier clase de tag NFC
		mTechLists = new String[][] { new String[] { NfcF.class.getName() } };

		Intent testIntent = getIntent();
		Log.d(TAG, "INTENT " + testIntent.toString());
		Parcelable[] rawMsgs = testIntent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		Log.d(TAG, "RAW " + rawMsgs);
		try{
		lectura(testIntent);
		}catch(Exception e){
			Log.e(TAG, "Exception::onCreate()->"+e.getMessage());
		}
	}

	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
		}
		
		Log.d(TAG,
				"Regresando de operacion a MainActivity conservando el TAGID "
						+ placaNo);
		// if (result != null) {
		// // new MiTarea(result).execute();
		// }
	}

	@Override
	public void onNewIntent(Intent intent) {
		try{
			lectura(intent);
			}catch(Exception e){
				Log.e(TAG, "Exception::onNewIntent()->"+e.getMessage());
			}
	}

	public void lectura(Intent intent) {
		super.onNewIntent(intent);
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||placaNo!=null) {
			NdefMessage[] messages = null;
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				messages = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					messages[i] = (NdefMessage) rawMsgs[i];
				}
			}

			if (messages[0] != null) {
				result = "";
				byte[] payload = messages[0].getRecords()[1].getPayload();
				// this assumes that we get back am SOH followed by host/code
				for (int b = 0; b < payload.length; b++) { // skip SOH
					result += (char) payload[b];
				}

				String uidTag = "";

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

				uidTag = bin2hex(tag.getId());
				Log.d(TAG, "Lectura UID: " + uidTag);

				String resultado = validaTag(payload, uidTag);
				String[] elementos;

				if (resultado != null) {
					elementos = resultado.split("\\|");
					placaNo = elementos[0];
					indice_entidad = Integer.parseInt(elementos[1]);
					indice_tipotransporte = Integer.parseInt(elementos[2]);
					indice_color = Integer.parseInt(elementos[3]);
					valid = elementos[4];
					/*
					 RFID001|00|20|0|1
					 */
					Log.d(TAG, "Datos de cedula: " + placaNo + " "
							+ indice_entidad + " " + indice_tipotransporte
							+ " " + indice_color + " " + valid);

					if (valid.equals("1")) {

						

						

						Resources res = getResources();
						
						String[] entidades = res
								.getStringArray(R.array.entidades);
						String[] tipostransporte = res
								.getStringArray(R.array.tipostransporte);
						
						String[] coloresplaca = res
								.getStringArray(R.array.coloresplaca);
						
						mText = (TextView) findViewById(R.id.dirterritorial);
						mText.setText(elementos[1] + " " + entidades[indice_entidad]);

						mText = (TextView) findViewById(R.id.dirpolicial);
						mText.setText(tipostransporte[indice_tipotransporte]);
						
						
						
						mText = (TextView) findViewById(R.id.label_tmpBalance);
						mText.setText(placaNo);
						
						
						String nombreRecursoEntidad = "entidad_"+indice_entidad;
						
						imagenEntidad = nombreRecursoEntidad;
						
						String nombreRecursoColorPlaca = "color_"+indice_color;
						 
						 int logoid = this.getResources().getIdentifier(nombreRecursoEntidad.toLowerCase(), "drawable", this.getPackageName());
						 
						 
						 ImageView logo = null;
						 logo = (ImageView)findViewById(R.id.image_title);
						 logo.setImageResource(logoid);
						 
						 logoid = this.getResources().getIdentifier(nombreRecursoColorPlaca.toLowerCase(), "drawable", this.getPackageName());
						 
						 logo = (ImageView)findViewById(R.id.color_placa);
						 logo.setImageResource(logoid);
						 if(indice_color!=0){
							 logo.setVisibility(android.view.View.VISIBLE);
						 }else{
							 logo.setVisibility(android.view.View.INVISIBLE);
						 }

						/*
						 * imageType = (ImageView)
						 * findViewById(R.id.image_typeLicense);
						 * if(type.equals("A")){
						 * imageType.setImageResource(R.drawable.typea); }else
						 * if(type.equals("B")){
						 * imageType.setImageResource(R.drawable.typeb); }
						 * imageType.setVisibility(android.view.View.VISIBLE);
						 */
						//Calendar c = Calendar.getInstance();
						/*
						 imageType = (ImageView) findViewById(R.id.image_licenseStatus);
						imageType.setImageResource(R.drawable.ok);
						imageType.setVisibility(android.view.View.VISIBLE);

*/
						 imageType = (ImageView) findViewById(R.id.image_licenseStatus);
							imageType.setImageResource(R.drawable.ok);
							imageType.setVisibility(android.view.View.INVISIBLE);
						activarBotones();

					} else {
						lecturaDatosNoValidos();
					}

				} else {
					lecturaDatosNoValidos();
				}

				/*
				 * mText = (TextView) findViewById(R.id.vehicle_name);
				 * mText.setText(result);
				 * 
				 * Log.d(TAG,bin2hex(getSHA2(result)));
				 * 
				 * // Para verificar que se encuentren los demas registros
				 * 
				 * result = ""; payload =
				 * messages[0].getRecords()[1].getPayload(); // this assumes
				 * that we get back am SOH followed by host/code for (int b = 0;
				 * b < payload.length; b++) { // skip SOH result += (char)
				 * payload[b]; }
				 * 
				 * Log.d(TAG, "Lectura NFC Fecha: " + result);
				 * 
				 * mText = (TextView) findViewById(R.id.label_tmpBalance);
				 * mText.setText(result);
				 * 
				 * result = ""; payload =
				 * messages[0].getRecords()[2].getPayload(); // this assumes
				 * that we get back am SOH followed by host/code for (int b = 0;
				 * b < payload.length; b++) { // skip SOH result += (char)
				 * payload[b]; }
				 * 
				 * Log.d(TAG, "Lectura NFC Valido: " + result);
				 */

				/*
				 * try{ decifrado = SimpleCrypto.decrypt("admin", cifrado);
				 * Log.d(TAG, "Cadena Decifrada " +decifrado);
				 * 
				 * }catch(Exception e){ e.printStackTrace(); }
				 */

				// byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				// Log.d(TAG, "Lectura UID[byte]: " + bin2hex(tagId));
				// new MiTarea(result).execute();

			}

		} else {
			alert(getString(R.string.nfc_welcome));
			desactivarBotones();

		}
	}

	protected void lecturaDatosNoValidos() {
		Log.d(TAG, "Documento NO valido");
		
		mText = (TextView) findViewById(R.id.label_tmpBalance);
		mText.setText("");

		
		imageType = (ImageView) findViewById(R.id.image_licenseStatus);
		imageType.setImageResource(R.drawable.no);
		imageType.setVisibility(android.view.View.VISIBLE);
		
		imageType = (ImageView)findViewById(R.id.color_placa);
		
		imageType.setVisibility(android.view.View.INVISIBLE);

		mText = (TextView) findViewById(R.id.dirterritorial);
		mText.setText("");

		mText = (TextView) findViewById(R.id.label_dirterritorial);
		mText.setText("");

		mText = (TextView) findViewById(R.id.dirpolicial);
		mText.setText("");

		mText = (TextView) findViewById(R.id.label_dirpolicial);
		mText.setText("");
		
		mText = (TextView) findViewById(R.id.label_tmpBalance);
		mText.setText("No Válido");

		desactivarBotones();

	}

	protected String validaTag(byte[] payload, String uidTag) {

		Log.d(TAG, "Lectura NFC Datos Cifrados: " + bin2hex(payload));

		String datosLeidos = "";

		try {

			byte[] key2 = SimpleCrypto.createkey(uidTag + "NeoPlacas");

			byte[] decryptedData = SimpleCrypto.decrypt(key2, payload);

			datosLeidos = new String(decryptedData);
			Log.d(TAG, "NFC::decryptedData: " + datosLeidos);

			return datosLeidos;

		} catch (Exception e) {
			Log.i(TAG,
					"LicenseValidator::validaTag: NFC Tag Corrupto->"
							+ e.getCause());
		}

		return null;
	}

	protected void activarBotones() {
		botonMain = (Button) findViewById(R.id.button_recharge);
		botonMain.setEnabled(true);
		
		mText = (TextView) findViewById(R.id.label_dirterritorial);
		mText.setText(getText(R.string.dirterritorial_label));
		mText = (TextView) findViewById(R.id.label_dirpolicial);
		mText.setText(getText(R.string.dirpolicial_label));
	}

	protected void desactivarBotones() {
		botonMain = (Button) findViewById(R.id.button_recharge);
		botonMain.setEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void openLicenseData(View view) {
		// Intent intentRecharge = new Intent(this, RechargeActivity.class);
		Intent intent = new Intent(this, LicenseDataActivity.class);
		Log.d(TAG, "Result para enviar a intent: " + placaNo + " "
				+ imagenEntidad);
		intent.putExtra("LICENSE_ID", placaNo);
		intent.putExtra("ENTIDAD_ID", imagenEntidad);
		//intent.putExtra("INDICE_POLICIAL", Integer.toString(indice_color));
		startActivity(intent);
	}

	/** Se manda a llamar cuando el usuario selecciona la opcion Leer */
	public void openReadNFC(View view) {

		Intent intentReadTest = new Intent(this, PersoTagActivity.class);
		// Intent intentRecharge = new Intent(this, ListRecharge.class);
		startActivity(intentReadTest);

	}

	public String readTag(Tag tag) {
		MifareUltralight mifare = MifareUltralight.get(tag);
		try {
			mifare.connect();
			byte[] payload = mifare.readPages(4);
			return new String(payload, Charset.forName("US-ASCII"));
		} catch (IOException e) {
			Log.e(TAG,
					"IOException while writing MifareUltralight  message...", e);
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.e(TAG, "Error closing tag...", e);
				}
			}
		}
		return null;
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d("alert", "Showing alert dialog: " + message);
		bld.create().show();
	}

	public byte[] getSHA2(String rawtext) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		digest.update(rawtext.getBytes());
		return digest.digest();
	}

	static String bin2hex(byte[] data) {
		return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,
				data));
	}

	private class MiTarea extends AsyncTask<String, Float, String> {

		protected final String id;

		public MiTarea(String id) {
			this.id = id;

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("id", id);
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			Log.v(request.toString(), envelope.toString());
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(URL);
			transporte.debug = true;

			String res = "respuestavacia";
			try {
				transporte.call(SOAP_ACTION, envelope);
				resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
				res = resultsRequestSOAP.toString();
				Log.v("response", res);

				/*
				 * mText = (TextView) findViewById(R.id.vehicle_name);
				 * mText.setText(elementos[1]);
				 * 
				 * mText = (TextView) findViewById(R.id.label_tmpBalance);
				 * mText.setText(elementos[2]);
				 */

			} catch (IOException e) {

				e.printStackTrace();
			} catch (XmlPullParserException e) {

				e.printStackTrace();
			}
			return res;
		}

		protected void onPostExecute(String response) {
			String[] elementos = response.split("\\|");
			try {
				Log.v("response", elementos[0]);
				Log.v("response", elementos[1]);
				Log.v("response", elementos[2]);

				
				NumberFormat formatter = NumberFormat
						.getCurrencyInstance(Locale.US);
				String moneyString = formatter.format(Double
						.valueOf(elementos[2]));
				// System.out.println(moneyString);

				mText = (TextView) findViewById(R.id.label_tmpBalance);
				mText.setText(moneyString);
				activarBotones();
			} catch (Exception e) {
				e.printStackTrace();
				desactivarBotones();
			}

			dialog.dismiss();
		}

	}

}
