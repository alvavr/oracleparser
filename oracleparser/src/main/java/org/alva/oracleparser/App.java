package org.alva.oracleparser;
import java.sql.*;
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
        
        
        Connection conn;
        try {
			conn = getConn("FALA_SBX","hernan");
			objs = getDbObject(conn, "PACKAGE");
			
            //Recorrer los packages
			for (String obj : objs) {
				String ddl = getDDL(conn, obj);
				parsePkg(obj);
			}
			
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
    }

	public static String getDDL(Connection conn,String obj) {
		// TODO Auto-generated method stub
		return null;
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

	public static ArrayList<String> getDbObject(Connection conn, String tobjeto) {
		// Las querys ir a buscarlas a un archivo Labels
		String labels_query = "select object_name from user_objects where object_type = ? order by object_name asc";
		ArrayList<String> objs = new ArrayList<String>();
		if(conn != null && tobjeto != null){ 
			try {
					PreparedStatement statement = conn.prepareStatement(labels_query);
					
					if(tobjeto.equals("PACKAGE")){
						statement.setString(1, "PACKAGE");
					}else if(tobjeto.equals("PROCEDIMIENTO")){
						statement.setString(1, "PROCEDURE");						
					}
					
					ResultSet r = statement.executeQuery();
					while(r.next()){
						objs.add(r.getString(1));
					}			
		            
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
		 
		return objs;
	}

	public ArrayList<String> buscaPks() {
		// TODO Implementar metodo para retornar un listado de los
		// procedimientos almacenados que
		// existen en la base de datos activa
		return null;
	}

	public ArrayList<String> buscaSps() {
		// TODO Implementar metodo para retornar un listado las funciones que
		// existen en la base de datos activa
		return null;
	}

	public void buscaTablas(String objeto) {
		// TODO Debe retornar una lista con todas las tablas que son editadas,
		// dado un tipo de objeto (pkg,sp,func).
	}

	public void buscaCampos(String objeto) {
		// TODO Debe retornar una lista con todos los campos que son editados,
		// dado un tipo de objeto (pkg,sp,func).
	}
	
	public static void parsePkg(String ddlpkg){
		//Parsea los paquetes en busca de sps, tablas y columnas
	}
}
