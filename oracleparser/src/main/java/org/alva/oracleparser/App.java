package org.alva.oracleparser;
import java.sql.*;
import java.util.regex.*;
import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Inicio programa: \n" );
        ArrayList<String> objs = null;
        
        
        Connection conn = null;
        try {
			conn = getConn("FALA_SBX","hernan");
			objs = getDbObject(conn, "PACKAGE");
			
            //Recorrer los packages
			//for (String obj : objs) {
				System.out.println("sps del objeto: "+objs.get(0));
				String ddl = getDDL(conn, "PACKAGE", objs.get(0));
				parsePkg(objs.get(0), ddl);
			//}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        
        
        
        
    }

	public static String getDDL(Connection conn, String tipo, String obj) throws SQLException{
		String labels_query = "select DBMS_METADATA.GET_DDL(?,?) from DUAL"; //PACKAGE,HAS_PKG_SFC
		String ddl = null;
		PreparedStatement statement = null;
		try {
			statement =  conn.prepareStatement(labels_query);
			statement.setString(1, tipo);
			statement.setString(2, obj);
			ResultSet r = statement.executeQuery();
			while(r.next()){
				ddl = r.getString(1);
			}	
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if (statement != null) {
				statement.close();
	        }
		}
		return ddl;
	}

	public static Connection getConn(String bd, String usuario) throws SQLException {
		//Estos valores estarán en un archivo Labels después***
		String prop_url = "jdbc:oracle:thin:@//localhost:20001/dbmk01";
        String prop_user = "hsaavedra";
        String prop_pass = "Enaccion1114";
        //*****************************************************
        
        String url = "";
        String user = "";
        String pass = "";
        if(bd != null && bd.equals("FALA_SBX")){
        	if(usuario != null && usuario.equals("hernan")){
        		url = prop_url;
            	user = prop_user;
            	pass = prop_pass;
        	}
        	
        }
        
        Connection conn = DriverManager.getConnection(url, user, pass);
        
		
		return conn;
	}

	public static ArrayList<String> getDbObject(Connection conn, String tobjeto) throws SQLException {
		// Las querys ir a buscarlas a un archivo Labels
		String labels_query = "select object_name from user_objects where object_type = ? order by object_name asc";
		ArrayList<String> objs = new ArrayList<String>();
		PreparedStatement statement = null;
		if(conn != null && tobjeto != null){ 
			try {
					statement = conn.prepareStatement(labels_query);
					
					if(tobjeto.equals("PACKAGE")){
						statement.setString(1, "PACKAGE");
					}else if(tobjeto.equals("PROCEDIMIENTO")){
						statement.setString(1, "PROCEDURE");						
					}//TODO continuar con los demas objetos
					
					ResultSet r = statement.executeQuery();
					while(r.next()){
						objs.add(r.getString(1));
					}			
		            
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					if (statement != null) {
						statement.close();
			        }
				}
		}
		 
		return objs;
	}

	public ArrayList<String> buscaPks() {
		// TODO Implementar metodo para retornar un listado de los
		// procedimientos almacenados que
		// existen en la base de datos activa
		return null;
	}

	public static ArrayList<String> buscaSps(String ddl) {
		//Busca si hay declaraciones de procedimientos almacenados en un texto
		ArrayList<String> spsEncontrados = new ArrayList<String>();
		ddl.replaceAll("PROCEDURE", "procedure");
		Matcher m = Pattern.compile("procedure.*.(.*.).*.;").matcher(ddl);
		String sp = null;
		while(m.find()){
			sp = m.group().substring(9, m.group().indexOf("("));
			sp = sp.trim();
			
			spsEncontrados.add(sp);
			//System.out.println(sp+" Inicio: "+m.start()+ " fin: "+m.end());
		}
		return spsEncontrados;
	}

	public void buscaTablas(String objeto) {
		// TODO Debe retornar una lista con todas las tablas que son editadas,
		// dado un tipo de objeto (pkg,sp,func).
	}

	public void buscaCampos(String objeto) {
		// TODO Debe retornar una lista con todos los campos que son editados,
		// dado un tipo de objeto (pkg,sp,func).
	}
	
	public static void parsePkg(String obj, String ddlpkg){
		//Parsea los paquetes en busca de sps, tablas y columnas
		ArrayList<String> sps = buscaSps(ddlpkg);
		for (String sp : sps) {
			System.out.println(sp);
		}
		
	}
}
