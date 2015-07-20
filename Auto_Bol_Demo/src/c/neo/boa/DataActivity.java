package c.neo.boa;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import c.neo.boa.fragments.FrenteFragment;
import c.neo.boa.fragments.ReversoFragment;
import c.neo.boa.pagetransformers.ZoomOutPageTransformer;
import c.neo.boa.utils.Auto;
import c.neo.boa.utils.CheckInternetConnection;
import c.neo.boa.R;

public class DataActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private final String TAG = DataActivity.class.getSimpleName();
	private String no_cedula = "";

	// Defined to Consume the WS
	private SoapObject request = null;
	private SoapSerializationEnvelope envelope = null;
	private SoapPrimitive resultsRequestSOAP = null;

	private static final String SOAP_ACTION = "";
	private static final String METHOD_NAME = "datosUsr";
	private static final String NAMESPACE = "http://ws.neo.c";
	private static final String URL = "http://192.168.0.14:8080/WS/services/ServiceAuto?wsdl";

	// Dialog to show a wait message
	private ProgressDialog dialog;
	// Dialog to show at download proccess
	private ProgressDialog dialogDownload;

	// Defined to UI values
	private TextView tvUI;
	private ImageView ivUI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);

		Intent intent = getIntent();

		no_cedula = intent.getStringExtra("NO_CEDULA");

		// Creacion del mensaje de espera
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.wait_label));
		dialog.setTitle(getString(R.string.gettingdata_label));

		dialogDownload = new ProgressDialog(this);
		dialogDownload.setMessage(getString(R.string.wait_label));
		dialogDownload.setTitle(getString(R.string.gettinimage_label));
		dialogDownload.setIndeterminate(false);
		dialogDownload.setMax(100);
		dialogDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Set up custom animation
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		// mViewPager.setPageTransformer(true, new DepthPageTransformer());

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		if (CheckInternetConnection.isConnectedToInternet(getApplicationContext())) {
//			new ConsultaWS().execute(no_cedula);
			new ws().execute(no_cedula);
		} else {
			Toast.makeText(getApplicationContext(), "No hay servicio de Internet", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new FrenteFragment();
				break;
			case 1:
				fragment = new ReversoFragment();
				break;
			case 2:
				break;
			}
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_data_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	private class ws extends AsyncTask<String, Float, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Object resultado = null;
			
			try {			

				Log.i(TAG, "Num de matricula a consultar: " + no_cedula);
				// Modelo el request
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("srtFolio", params[0]); // Paso parametros al WS

				// Modelo el Sobre
				SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				sobre.setOutputSoapObject(request);

				// Modelo el transporte
				HttpTransportSE transporte = new HttpTransportSE(URL);

				// Llamada
				transporte.call(SOAP_ACTION, sobre);

				// Resultado
				resultado = sobre.getResponse();

				Log.i("Resultado", resultado.toString());

			} catch (Exception e) {
				Log.e("ERROR", e.getMessage());
			}
			return resultado.toString();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result != null) {
				try {
					String[] elementos = result.split("\\|");
					Auto autosObj = new Auto(elementos);
					Log.d(TAG, "Folio Auto: " + autosObj.getStrFolio());
					setUIDatosFrente(autosObj);
				}catch(Exception e) {
					
				}
			}
		}
		
	}

	public void setUIDatosFrente(Auto auto) {
		tvUI = (TextView) findViewById(R.id.nombre);
		tvUI.setText(auto.getStrFolio());
		tvUI = (TextView) findViewById(R.id.apellidos);
		tvUI.setText(auto.getStrMarca());
		tvUI = (TextView) findViewById(R.id.profesion);
		tvUI.setText(auto.getStrSubMarca());
		tvUI = (TextView) findViewById(R.id.estado_civil);
		tvUI.setText(auto.getStrPlaca());
		tvUI = (TextView) findViewById(R.id.fecha_nacimiento);
		tvUI.setText(auto.getStrAnioModelo());
		tvUI = (TextView) findViewById(R.id.fecha_emision);
		tvUI.setText(auto.getStrImgAuto());
		tvUI = (TextView) findViewById(R.id.fecha_vencimiento);
		tvUI.setText(auto.getDtmFechaExpiracion());
		tvUI = (TextView) findViewById(R.id.domicilio);
		tvUI.setText(auto.getStrTipoAuto());
	}

	/**
	 * Background Async Task to download file
	 * */
	private class DownloadFileFromURL extends AsyncTask<String, String, String> {

		String tipo = "";

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogDownload.show();
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);

				tipo = f_url[2];

				URLConnection conection = url.openConnection();
				conection.connect();
				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = conection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				Log.d(TAG, "Almacenar en : "
						+ Environment.getExternalStorageDirectory().getPath());
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().getPath() + "/" + tipo);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

				ivUI = (ImageView) findViewById(Integer.parseInt(f_url[1]));

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			dialogDownload.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dialogDownload.dismiss();

			// Displaying downloaded image into image view
			// Reading image path from sdcard
			try {
				String imagePath = Environment.getExternalStorageDirectory()
						.toString() + "/" + tipo;
				// setting downloaded into image view
				// ImageView my_image = (ImageView)findViewById(R.id.foto_img);
				
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap preview_bitmap=BitmapFactory.decodeFile(imagePath, options);
				
				ivUI.setImageBitmap(preview_bitmap);
			} catch (Exception e) {
				Log.e(TAG,
						"Exception::DownloadFileFromURL:onPostExecute->"
								+ e.getMessage());
			}
		}

	}

}
