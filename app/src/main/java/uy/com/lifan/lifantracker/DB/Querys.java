package uy.com.lifan.lifantracker.DB;

public class Querys {

    //Clase que agrupa todas las sentencias SQL usadas en la app, para un facil acceso, control y modificación.

    //CONSULTAS

    public static final String QRY_USUARIOS_ACTIVOS = "SELECT name FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and order by 1 ";
    public static final String  QRY_USUARIO  = "SELECT emailuserpw FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and name = '%s' ";
    public static final String  QRY_LOCATIONS  = "SELECT * FROM android.lifan_android_locations ";
    public static final String QRY_DATOS_VIN = "select  line,z_enddate,z_shipment,(select value from m_product where m_product_id= pp.m_product_id),z_vin,z_engine,z_ecuengine,z_startdate, z_enddate, z_key from m_productionplan pp where isactive='Y' and z_vin = '%s'";
    public static final String  INSERT_LOCATION  = "INSERT INTO android.lifan_android_locations (location_id, vin, latitud, longitud, ad_org_id, ad_client_id, isactive, created, createdby, updated, updatedby, islast) VALUES (NEXTID(2100480,'N'), '%s', %f, %f, 2100002, 2100001,'Y', now(), 2100265, now(), 2100265, 'Y'); ";



}
