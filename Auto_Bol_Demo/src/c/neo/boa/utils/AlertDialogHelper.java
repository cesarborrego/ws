package c.neo.boa.utils;

import c.neo.boa.utils.constants.Constants;
import c.neo.boa.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * 
 * @author Cesar Segura Granados
 *
 */
public class AlertDialogHelper {
	public static AlertDialog showAlertDialogConfigs(int dialogType, final Activity a) {

		final Intent intent = new Intent();
		String mensaje = "";

		switch(dialogType){
		case Constants.NFC_SETTINGS_DIALOG:
			intent.setAction(Settings.ACTION_NFC_SETTINGS);
			mensaje = a.getResources().getString(R.string.conexionNFCDesactivada);
			break;
		case Constants.WIRELESS_SETTINGS_DIALOG:
			intent.setAction(Settings.ACTION_SETTINGS);
			mensaje = a.getResources().getString(R.string.conexionRedDesactivada);
			break;
		case Constants.GPS_SETTINGS_DIALOG:
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mensaje = a.getResources().getString(R.string.gpsDesactivado);
			break;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle(R.string.titleAlertDialog);
		builder.setMessage(mensaje)
		.setPositiveButton(R.string.btnTextAceptar,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
				a.startActivity(intent);
			}

		})
		.setNegativeButton(R.string.btnTextCancelar,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.cancel();
				//				finish();
			}
		});
		AlertDialog alertDialog = builder.create();
		return alertDialog;
	}
}
