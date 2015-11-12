package uy.com.lifan.lifantracker.DB;

public class Querys {

    //Clase que agrupa todas las sentencias SQL usadas en la app, par auna ms facil modificacion.

    public static final String  USUARIOS_ACTIVOS  = "SELECT name FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and order by 1 ";

    public static final String  QRY_USUARIO  = "SELECT emailuserpw FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and name = '%s' ";
    public static final String  QRY_LOCATIONS  = "SELECT * FROM android.lifan_android_locations ";
    public static final String  INSERT_LOCATION  = "INSERT INTO android.lifan_android_locations (location_id, vin, latitud, longitud, ad_org_id, ad_client_id, isactive, created, createdby, updated, updatedby, islast) VALUES (NEXTID(2100480,'N'), '%s', %f, %f, 2100002, 2100001,'Y', now(), 2100265, now(), 2100265, 'Y'); ";


 //   "INSERT INTO usuario (nome, email, telefone) VALUES ('%s','%s','%s')

}
