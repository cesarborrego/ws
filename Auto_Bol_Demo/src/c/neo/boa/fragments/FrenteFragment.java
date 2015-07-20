package c.neo.boa.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import c.neo.boa.R;

public class FrenteFragment extends Fragment{

	Context context;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_frente,
				container, false);
		context = (Context) getArguments().get("");
		
		ViewHolder viewHolder = new ViewHolder();

		viewHolder.contenedor = (LinearLayout)rootView.findViewById(R.id.contenedorPrincipal);
		viewHolder.foto = (ImageView)rootView.findViewById(R.id.foto_img);
		viewHolder.cedulaLbl = (TextView)rootView.findViewById(R.id.lblCedulaID);
		viewHolder.cedulaTxt = (TextView)rootView.findViewById(R.id.txtCedulaID);
		viewHolder.nombreLbl = (TextView)rootView.findViewById(R.id.label_nombre);
		viewHolder.nombreTxt = (TextView)rootView.findViewById(R.id.nombre);
		viewHolder.apellidosLbl = (TextView)rootView.findViewById(R.id.label_apellidos);
		viewHolder.apellidosTxt = (TextView)rootView.findViewById(R.id.apellidos);
		viewHolder.profesionLbl = (TextView)rootView.findViewById(R.id.label_profesion);
		viewHolder.profesionTxt = (TextView)rootView.findViewById(R.id.profesion);
		viewHolder.estadoCivilLbl = (TextView)rootView.findViewById(R.id.label_estado_civil);
		viewHolder.estadoCivilTxt = (TextView)rootView.findViewById(R.id.estado_civil);
		viewHolder.fechaNacLbl = (TextView)rootView.findViewById(R.id.label_fecha_nacimiento);
		viewHolder.fechaNacTxt = (TextView)rootView.findViewById(R.id.fecha_nacimiento);
		viewHolder.lugarNacLbl = (TextView)rootView.findViewById(R.id.label_lugar_nacimiento);
		viewHolder.lugarNacTxt = (TextView)rootView.findViewById(R.id.lugar_nacimiento);
		viewHolder.lblMatricula = (TextView)rootView.findViewById(R.id.lblMatricula);
		viewHolder.fotoMini = (ImageView)rootView.findViewById(R.id.imgFantasmaID);

		rootView.setTag(viewHolder);
		
		viewHolder.fotoMini.setAlpha(85);

		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 5;
		Bitmap preview_bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.bolivia_flag, options);
		
		viewHolder.contenedor.setBackground(new BitmapDrawable(preview_bitmap));
		viewHolder.contenedor.setAlpha(70);

		return rootView;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity.getApplicationContext();
		Log.d("FrenteFragment", "Contexto asignado");
	}

	static class ViewHolder {
		LinearLayout contenedor;
		ImageView fotoMini;
		TextView cedulaLbl;
		TextView cedulaTxt;
		TextView nombreLbl;
		TextView nombreTxt;
		TextView apellidosLbl;
		TextView apellidosTxt;
		TextView profesionLbl;
		TextView profesionTxt;
		TextView estadoCivilLbl;
		TextView estadoCivilTxt;
		TextView fechaNacLbl;
		TextView fechaNacTxt;
		TextView lugarNacLbl;
		TextView lugarNacTxt;
		TextView domicilioTxt;
		TextView domicilioLbl;
		TextView lblMatricula;
		ImageView foto;
	}
}
