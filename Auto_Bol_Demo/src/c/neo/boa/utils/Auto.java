package c.neo.boa.utils;

public class Auto {
	
	String strFolio, 
	strMarca, 
	strSubMarca, 
	strPlaca, 
	strAnioModelo, 
	strImgAuto, 
	dtmFechaExpiracion,
	strTipoAuto;
	
	public Auto(){}
	
	public Auto(String[] elementos){
		this.strFolio = elementos[0];
		this.strMarca = elementos[1];
		this.strSubMarca = elementos[2];
		this.strPlaca = elementos[3];
		this.strAnioModelo = elementos[4];
		this.strImgAuto = elementos[5];
		this.dtmFechaExpiracion= elementos[6];
		this.strTipoAuto= elementos[7];

	}

	public String getStrFolio() {
		return strFolio;
	}

	public void setStrFolio(String strFolio) {
		this.strFolio = strFolio;
	}

	public String getStrMarca() {
		return strMarca;
	}

	public void setStrMarca(String strMarca) {
		this.strMarca = strMarca;
	}

	public String getStrSubMarca() {
		return strSubMarca;
	}

	public void setStrSubMarca(String strSubMarca) {
		this.strSubMarca = strSubMarca;
	}

	public String getStrPlaca() {
		return strPlaca;
	}

	public void setStrPlaca(String strPlaca) {
		this.strPlaca = strPlaca;
	}

	public String getStrAnioModelo() {
		return strAnioModelo;
	}

	public void setStrAnioModelo(String strAnioModelo) {
		this.strAnioModelo = strAnioModelo;
	}

	public String getStrImgAuto() {
		return strImgAuto;
	}

	public void setStrImgAuto(String strImgAuto) {
		this.strImgAuto = strImgAuto;
	}

	public String getDtmFechaExpiracion() {
		return dtmFechaExpiracion;
	}

	public void setDtmFechaExpiracion(String dtmFechaExpiracion) {
		this.dtmFechaExpiracion = dtmFechaExpiracion;
	}

	public String getStrTipoAuto() {
		return strTipoAuto;
	}

	public void setStrTipoAuto(String strTipoAuto) {
		this.strTipoAuto = strTipoAuto;
	}
}
