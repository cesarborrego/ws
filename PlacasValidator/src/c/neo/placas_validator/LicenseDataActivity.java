package c.neo.placas_validator;

import java.io.IOException;
import java.io.InputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import c.neo.placas_validator.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LicenseDataActivity extends Activity {

	private static final String TAG = LicenseDataActivity.class.getSimpleName();

	// Declaracion de variables para consumir el web service
	private SoapObject request = null;
	private SoapSerializationEnvelope envelope = null;
	private SoapPrimitive resultsRequestSOAP = null;

	private static final String SOAP_ACTION = "";
	private static final String METHOD_NAME = "getPlacasData";
	private static final String NAMESPACE = "http://impl.neology.com/";
	private static final String URL = "http://148.245.107.245:8095/WSConexionDB/services/transactiondetailservice?wsdl";

	private String urlFoto = "";
	private String urlHuella = "";
	private String licenseID = "";
	private String idEntidadImagen = "";
	// Mensaje de espera
	private ProgressDialog dialog;
	
	SlidingPaneLayout slidingPaneLayout; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_license_data);
/*
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
*/
		// Creacion del mensaje de espera
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.wait_label));
		dialog.setTitle(getString(R.string.gettingdata_label));

		Intent intent = getIntent();
		 licenseID = intent.getStringExtra("LICENSE_ID");
		 idEntidadImagen = intent.getStringExtra("ENTIDAD_ID");

		 //String nombreRecursoPolicial = "dir_"+indice_policial;
		 
		 /*
		 int logoid = this.getResources().getIdentifier(nombreRecursoPolicial.toLowerCase(), "drawable", this.getPackageName());
		
		 
		 ImageView logo = null;
		 logo = (ImageView)findViewById(R.id.idEntidadImagen);
		 logo.setImageResource(idEntidadImagen);
		 
		 logo = (ImageView)findViewById(R.id.escudo_rd);
		 logo.setImageResource(logoid);
		 */
		 
		 ImageView logo = null;
		 logo = (ImageView)findViewById(R.id.image_photo);
		 int logoid = this.getResources().getIdentifier(idEntidadImagen, "drawable", this.getPackageName());
		 logo.setImageResource(logoid);
		 
		TextView text = (TextView) findViewById(R.id.license_number);
		text.setText(""+licenseID);
		
		/*

		slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingpanelayout);
		
		//slidingPaneLayout.setSliderFadeColor(android.graphics.Color.parseColor("#C68A34"));
		slidingPaneLayout.setParallaxDistance(60);
		
		slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

					@Override
					public void onPanelSlide(View view, float v) {
						//Log.d(TAG, "onPanelSlide " + view.getId());
					}

					@Override
					public void onPanelOpened(View view) {

						switch (view.getId()) {
						
						case R.id.sliding_b:
							Log.d(TAG, "onPanelOpened B");
							ActionBar actionBar = getActionBar();
							actionBar.setDisplayHomeAsUpEnabled(false);
							break;
						default:
							break;
						}
					}

					@Override
					public void onPanelClosed(View view) {

						switch (view.getId()) {
						
						case R.id.sliding_b:
							Log.d(TAG, "onPanelClosed B");
							ActionBar actionBar = getActionBar();
							actionBar.setDisplayHomeAsUpEnabled(true);
							break;
						default:
							break;
						}
					}
				});
		
		*/

		MiTarea obtenerDatos = new MiTarea(licenseID);

		obtenerDatos.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.license_data, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			

			Log.d(TAG, "HOME BUTTON SELECTED");
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			// app icon in action bar clicked; go home
			 Intent intent = new Intent(this, MainActivity.class);
			 intent.putExtra("PLACA_NO", licenseID);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intent);
			//slidingPaneLayout.openPane();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog.show();
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Log.d(TAG, "URL para descargar: " + urls[0]);
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
				in.close();
			} catch (Exception e) {
				Log.e("Error", e.getMessage()); 
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			dialog.dismiss();
		}
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
			transporte.debug = false;

			String res = null;
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

			try {

				if (response != null) {
					String[] elementos = response.split("\\|");
					Log.v(TAG, "strPlaca " + elementos[0]);
					Log.v(TAG, "strVin " + elementos[1]);
					Log.v(TAG, "strMarca " + elementos[2]);
					Log.v(TAG, "strSubmarca " + elementos[3]);
					Log.v(TAG, "intModelo " + elementos[4]);
					Log.v(TAG, "strUsoVehiculo " + elementos[5]);
					Log.v(TAG, "strVersion " + elementos[6]);
					Log.v(TAG, "strClase " + elementos[7]);
					Log.v(TAG, "strTipo " + elementos[8]);
					Log.v(TAG, "strColor " + elementos[9]);
					Log.v(TAG, "strNombre " + elementos[10]);
					Log.v(TAG, "strApellidos " + elementos[11]);
					
					

					//urlFoto = elementos[16];
					/*
					 * new DownloadImageTask((ImageView)
					 * findViewById(R.id.image_photo))
					 * .execute("http://148.245.107.245:8095/"+elementos[7]);
					 */

					TextView mText = null;
					
					
					//Panel Inicial
					mText = (TextView) findViewById(R.id.apellidos);
					mText.setText(elementos[1]);

					mText = (TextView) findViewById(R.id.nombre);
					mText.setText(elementos[2]);

					mText = (TextView) findViewById(R.id.fechanac);
					mText.setText(elementos[4]);
					
					mText = (TextView) findViewById(R.id.submarca);
					mText.setText(elementos[3]);
					
					mText = (TextView) findViewById(R.id.version);
					mText.setText(elementos[6]);
					
					
					mText = (TextView) findViewById(R.id.sexo);
					mText.setText(elementos[7] + ", "+ elementos[8]);
					
					mText = (TextView) findViewById(R.id.color);
					mText.setText(elementos[9]);
					
					mText = (TextView) findViewById(R.id.uso);
					mText.setText(elementos[5]);

					
					mText = (TextView) findViewById(R.id.apellidosp);
					mText.setText(elementos[11]);
					
					mText = (TextView) findViewById(R.id.nombrep);
					mText.setText(elementos[10]);
					/*
					mText = (TextView) findViewById(R.id.expedicion);
					mText.setText(elementos[5]);

					mText = (TextView) findViewById(R.id.expiracion);
					mText.setText(elementos[6]);
					*/

					// mText = (TextView) findViewById(R.id.vehicle_name);
					// mText.setText(elementos[1]);

					// System.out.println(moneyString);

					// mText = (TextView) findViewById(R.id.label_tmpBalance);
					// mText.setText(moneyString);

				} else {
					Log.d(TAG, "No hubo conexión...Reintentando 1");
					MiTarea obtenerDatos = new MiTarea(licenseID);

					obtenerDatos.execute();
					
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			dialog.dismiss();
			/*new DownloadImageTask((ImageView) findViewById(R.id.image_photo))
					.execute("http://148.245.107.245:8095/" + urlFoto);
			*/
		}

	}

}
