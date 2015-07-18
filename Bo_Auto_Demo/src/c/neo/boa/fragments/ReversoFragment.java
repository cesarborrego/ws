package c.neo.boa.fragments;

import android.graphics.Typeface;
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
import c.neo.boa.fragments.FrenteFragment.ViewHolder;

public class ReversoFragment extends Fragment{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_reverso,	container, false);
		
		ViewHolder viewHolder = new ViewHolder();
		
		viewHolder.logoNeoImg = (ImageView)rootView.findViewById(R.id.logoNeoID);
		viewHolder.huellaImg = (ImageView)rootView.findViewById(R.id.huellaImgID);
		viewHolder.firmaPersonaImg = (ImageView)rootView.findViewById(R.id.firmaID);
		viewHolder.logoSmartracImg = (ImageView)rootView.findViewById(R.id.logoSmartracID);
		viewHolder.firmaGabineteImg = (ImageView)rootView.findViewById(R.id.firmaJefeGabinete);
		viewHolder.firmaDepImg = (ImageView)rootView.findViewById(R.id.firmaDirDept);
		viewHolder.cedulaLbl = (TextView)rootView.findViewById(R.id.lblCedula);
		viewHolder.cedulaTxt = (TextView)rootView.findViewById(R.id.txtCedula);
		viewHolder.serieLbl = (TextView)rootView.findViewById(R.id.label_serie);
		viewHolder.serieTxt = (TextView)rootView.findViewById(R.id.serie);
		viewHolder.seccionLbl = (TextView)rootView.findViewById(R.id.label_seccion);
		viewHolder.seccionTxt= (TextView)rootView.findViewById(R.id.seccion);
		viewHolder.lugarEmisionLbl = (TextView)rootView.findViewById(R.id.lugarEmisionLbl);
		viewHolder.lugarEmisionTxt = (TextView)rootView.findViewById(R.id.lugarEmisionTxt);
		viewHolder.jefeGabineteLbl = (TextView)rootView.findViewById(R.id.jefeGabineteLbl);
		viewHolder.dirDeptLbl = (TextView)rootView.findViewById(R.id.dirDepLbl);
		viewHolder.matriculaLbl = (TextView)rootView.findViewById(R.id.matriculaID);
		viewHolder.ocr1 = (TextView)rootView.findViewById(R.id.ocr1ID);
		viewHolder.ocr2 = (TextView)rootView.findViewById(R.id.ocr2ID);
		viewHolder.ocr3 = (TextView)rootView.findViewById(R.id.ocr3ID);
		Log.d("FrenteFragment", "Contexto usado");
		
		Typeface typeFace=Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),"fonts/OCRAEXT.TTF");
		viewHolder.ocr1.setTypeface(typeFace);		
		viewHolder.ocr2.setTypeface(typeFace);
		viewHolder.ocr3.setTypeface(typeFace);
		
		rootView.setTag(viewHolder);
		
		return rootView;
	}
	
	static class ViewHolder {
		ImageView logoNeoImg;
		ImageView huellaImg;
		ImageView firmaPersonaImg;
		ImageView logoSmartracImg;
		ImageView firmaGabineteImg;
		ImageView firmaDepImg;
		TextView cedulaLbl;
		TextView cedulaTxt;
		TextView serieLbl;
		TextView serieTxt;
		TextView seccionLbl;
		TextView seccionTxt;
		TextView lugarEmisionLbl;
		TextView lugarEmisionTxt;
		TextView jefeGabineteLbl;
		TextView dirDeptLbl;
		TextView matriculaLbl;
		TextView ocr1;
		TextView ocr2;
		TextView ocr3;
	}

}
