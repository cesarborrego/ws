package c.neo.boa.utils;

public class Cedula {
	
	String intNoCedula, 
	strMatricula, 
	dateFechaEmision, 
	dateFechaExpiracion, 
	intSerie, 
	intSeccion, 
	intLugarEmision,
	strClaveEmision,
	strNombreEmision,
	strNombre,
	strApellidos,
	strProfesion,
	strDomicilio,
	strEstadoCivil,
	dateFechaNacimiento,
	strLugarNacimiento,
	strOCR1,
	strOCR2, 
	strOCR3,
	strURLfoto,
	strURLfirma,
	strURLhuella;
	
	public Cedula(){}
	
	public Cedula(String[] elementos){
		this.intNoCedula = elementos[0];
		this.strMatricula = elementos[1];
		this.dateFechaEmision = elementos[2];
		this.dateFechaExpiracion = elementos[3];
		this.intSerie = elementos[4];
		this.intSeccion = elementos[5];
		this.intLugarEmision= elementos[6];
		this.strClaveEmision= elementos[7];
		this.strNombreEmision= elementos[8];
		this.strNombre= elementos[9];
		this.strApellidos= elementos[10];
		this.strProfesion= elementos[11];
		this.strDomicilio= elementos[12];
		this.strEstadoCivil= elementos[13];
		this.dateFechaNacimiento= elementos[14];
		this.strLugarNacimiento= elementos[15];
		this.strOCR1 = elementos[16];
		this.strOCR2 = elementos[17];
		this.strOCR3= elementos[18];
		this.strURLfoto= elementos[19];
		this.strURLfirma= elementos[20];
		this.strURLhuella= elementos[21];

	}

	public String getIntNoCedula() {
		return intNoCedula;
	}

	public void setIntNoCedula(String intNoCedula) {
		this.intNoCedula = intNoCedula;
	}

	public String getStrMatricula() {
		return strMatricula;
	}

	public void setStrMatricula(String strMatricula) {
		this.strMatricula = strMatricula;
	}

	public String getDateFechaEmision() {
		return dateFechaEmision;
	}

	public void setDateFechaEmision(String dateFechaEmision) {
		this.dateFechaEmision = dateFechaEmision;
	}

	public String getDateFechaExpiracion() {
		return dateFechaExpiracion;
	}

	public void setDateFechaExpiracion(String dateFechaExpiracion) {
		this.dateFechaExpiracion = dateFechaExpiracion;
	}

	public String getIntSerie() {
		return intSerie;
	}

	public void setIntSerie(String intSerie) {
		this.intSerie = intSerie;
	}

	public String getIntSeccion() {
		return intSeccion;
	}

	public void setIntSeccion(String intSeccion) {
		this.intSeccion = intSeccion;
	}

	public String getIntLugarEmision() {
		return intLugarEmision;
	}

	public void setIntLugarEmision(String intLugarEmision) {
		this.intLugarEmision = intLugarEmision;
	}

	public String getStrClaveEmision() {
		return strClaveEmision;
	}

	public void setStrClaveEmision(String strClaveEmision) {
		this.strClaveEmision = strClaveEmision;
	}

	public String getStrNombreEmision() {
		return strNombreEmision;
	}

	public void setStrNombreEmision(String strNombreEmision) {
		this.strNombreEmision = strNombreEmision;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public String getStrApellidos() {
		return strApellidos;
	}

	public void setStrApellidos(String strApellidos) {
		this.strApellidos = strApellidos;
	}

	public String getStrProfesion() {
		return strProfesion;
	}

	public void setStrProfesion(String strProfesion) {
		this.strProfesion = strProfesion;
	}

	public String getStrDomicilio() {
		return strDomicilio;
	}

	public void setStrDomicilio(String strDomicilio) {
		this.strDomicilio = strDomicilio;
	}

	public String getStrEstadoCivil() {
		return strEstadoCivil;
	}

	public void setStrEstadoCivil(String strEstadoCivil) {
		this.strEstadoCivil = strEstadoCivil;
	}

	public String getDateFechaNacimiento() {
		return dateFechaNacimiento;
	}

	public void setDateFechaNacimiento(String dateFechaNacimiento) {
		this.dateFechaNacimiento = dateFechaNacimiento;
	}

	public String getStrLugarNacimiento() {
		return strLugarNacimiento;
	}

	public void setStrLugarNacimiento(String strLugarNacimiento) {
		this.strLugarNacimiento = strLugarNacimiento;
	}

	public String getStrOCR1() {
		return strOCR1;
	}

	public void setStrOCR1(String strOCR1) {
		this.strOCR1 = strOCR1;
	}

	public String getStrOCR2() {
		return strOCR2;
	}

	public void setStrOCR2(String strOCR2) {
		this.strOCR2 = strOCR2;
	}

	public String getStrOCR3() {
		return strOCR3;
	}

	public void setStrOCR3(String strOCR3) {
		this.strOCR3 = strOCR3;
	}

	public String getStrURLfoto() {
		return strURLfoto;
	}

	public void setStrURLfoto(String strURLfoto) {
		this.strURLfoto = strURLfoto;
	}

	public String getStrURLfirma() {
		return strURLfirma;
	}

	public void setStrURLfirma(String strURLfirma) {
		this.strURLfirma = strURLfirma;
	}

	public String getStrURLhuella() {
		return strURLhuella;
	}

	public void setStrURLhuella(String strURLhuella) {
		this.strURLhuella = strURLhuella;
	}
	
	

}
