package uy.com.lifan.lifantracker.DB;

public class Querys {

    //Clase que agrupa todas las sentencias SQL usadas en la app, para un facil acceso, control y modificación.

    //CONSULTAS QRY
    //INSERT INRT
    //UPDATE  UPT

    public static final String QRY_USUARIOS_ACTIVOS = "SELECT name FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and order by 1 ";
    public static final String QRY_USUARIO = "SELECT Smartphone_pin FROM adempiere.ad_user where ad_client_id = 2100001 and isactive= 'Y' and name = '%s' ";
    public static final String QRY_LOCATIONS = "SELECT * FROM android.lifan_android_locations ";
    public static final String QRY_LOCATIONS_VIN = "SELECT * FROM android.lifan_android_locations where vin = '%s' order by created desc limit 1";
    public static final String QRY_DATOS_VIN = "select  line,z_enddate::date,z_shipment,(select value from m_product where m_product_id= pp.m_product_id)as modelo,z_vin,z_engine,z_ecuengine,z_startdate, z_enddate, lifan_keycode,color,(select proceso from effa_li where effa_li_id =(select effa_li_id from effa_li_linea where vin = trim('%s')) ) as proceso,(select invoiceci from effa_li where effa_li_id =(select effa_li_id from effa_li_linea where vin = trim('%s')) ) as invoice from m_productionplan pp where isactive='Y' and z_vin = trim( '%s')";
    public static final String INRT_LOCATION = "INSERT INTO android.lifan_android_locations (location_id, vin, latitud, longitud, ad_org_id, ad_client_id, isactive, created, createdby, updated, updatedby, islast,date_las_gps_location) VALUES (NEXTID(2100480,'N'), '%s', %f, %f, 2100002, 2100001,'Y', now(), 2100265, now(), 2100265, 'Y','%s'); ";
    public static final String QRY_ADVANCED_SEARCH = "select * from android.lifan_android_locations loc, m_productionplan pp, m_product p where  pp.m_product_id=p.m_product_id and pp.z_vin = loc.vin and pp.z_vin ilike '%%%s%%' and z_engine ilike '%%%s%%' and color =trim('%s') and  p.M_Product_category_id=2100017 and p.m_attributeset_id=2100018 and p.value = '%s'";


}
