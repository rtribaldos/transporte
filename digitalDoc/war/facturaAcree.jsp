<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="author" content="Raul Tribaldos"/>
<meta name="description" content="Garceray"/>
<meta name="keywords" content="Garceray, transporte, camiones"/>
<meta name="language" content="spanish"/>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.digitaldoc.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	//String uploadURL = blobstoreService.createUploadUrl("/upload");
	//System.out.println("** url Upload: " + uploadURL);

	Usuario usuario = (Usuario) request.getAttribute("usuario");
	Documento factura = (Documento) request.getAttribute("factura");
		
	String styleDivDocs="display: block; visibility: visible;";
	String serie="";
	String idFactura="";
	String fecha="";
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	String numFactura="";
	String nomFichero="";
	String importe="";
	Usuario empresa=null;
	String habilitado="";
	
	if (factura==null){
		styleDivDocs="display: none; visibility: hidden;";
	}else{
		idFactura = factura.getId();
		if(factura.getFecha() != null){
			fecha = df.format(factura.getFecha());
		}
		numFactura= factura.getNumero();
		nomFichero =factura.getFichPrincipal();
		importe= String.valueOf(factura.getImporte());
		empresa = factura.getUsuario();
		serie=factura.getSerie();
		habilitado="readonly";
	}
%>

	<title>Garceray</title>

	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />

	<script src="assets/js/jquery-1.10.2.js"></script>
	<script src="assets/js/jquery-ui-1.10.4.custom.js"></script>

    <!-- Bootstrap core CSS     -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />

    <!-- Animation library for notifications   -->
    <link href="assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="assets/css/light-bootstrap-dashboard.css" rel="stylesheet"/>

    <!--  CSS for Demo Purpose, don't include it in your project     -->
    <link href="assets/css/demo.css" rel="stylesheet" />

    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />
</head>
<body>

<div class="wrapper">
    <div class="sidebar" data-color="green" data-image="assets/img/garceray/logo_transparente.png">

    <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


    	<div class="sidebar-wrapper">
            <div class="logo">
                <a href="#" class="simple-text">
                    Administraci&oacute;n 
                </a>
            </div>

             <ul class="nav">
                
                 <li class="active">
                			<a href="#">
                    		<i class="pe-7s-news-paper"></i>
                    		<p>Factura</p>
               	 		</a>
           		</li>
           		<li>
                 		<a href="/digitalDoc?op=anticipos&id=<%=usuario.getId()%>">
                     		<i class="pe-7s-news-paper"></i>
                     		<p>Anticipos</p>
               	 		</a>
           		</li>
           		<li>
                 		<a href="/digitalDoc?op=cancelaciones&id=<%=usuario.getId()%>">
                     		<i class="pe-7s-news-paper"></i>
                     		<p>Cancelaciones</p>
               	 		</a>
           		</li>
           		
         	</ul>
         	 

            
         	
    	</div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default navbar-fixed">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">Nueva factura</a>
                </div>
                
            </div>
        </nav>

        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Datos de la factura</h4>
                            </div>
                            <div class="content">
                            
                              
                               
                                    <div class="row">
                                        <div class="col-md-9">
                                            <div class="form-group">
                                                <label>Empresa</label>
                                                <select class="form-control" placeholder="Empresa" required name="empresa" <%=habilitado%>>
                                                	
                                                <%
                                                	if(empresa != null){
                                                %>
                                                	   <option value=<%=empresa.getId()%>><%=empresa.getEmpresa()%></option>
                                                <%		
                                                	}
                                                	
                                               	%>
                                               		
                                                </select>
                                                
                                            </div>
                                        </div>
                                       
                                        
                                        
                                    </div>
                                    <div class="row">
                                    	<div class="col-md-3">
                                            <div class="form-group">
                                                <label>Fecha</label>
                                                <input type="text" class="form-control" placeholder="Fecha" required name="fecha" value="<%=fecha%>" readonly>
                                            </div>
                                        </div>
                                    	<div class="col-md-3">
                                            <div class="form-group">
                                                <label>Serie</label>
                                                <input type="text" class="form-control" placeholder="Serie" name="serie" required value="<%=serie%>" readonly>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">N&uacute;mero</label>
                                                <input type="text" class="form-control" placeholder="N&uacute;mero" required name="numero" value="<%=numFactura%>" readonly>
                                            </div>
                                        </div>
                                    	<div class="col-md-3">
                                            <div class="form-group">
                                                <label>Importe</label>
                                                <input type="text" class="form-control" placeholder="Importe" name="importe" required value="<%=importe%>" readonly>
                                            </div>
                                        </div>
                                    </div>

									<div class="form-group">
                                      <label for="fichero">Factura >> <%=nomFichero%></label>
                                    </div>	

								    <div class="clearfix"></div>
                                
                                
                                <br>
                                
                                                               
                            </div>
                            
                            <div class="header" style="<%=styleDivDocs%>">
                                <h4 class="title">Documentos</h4>
                            </div>
                            <div class="content" style="<%=styleDivDocs%>">
                                
                                   <table id="tabla" class="table table-hover" width="100%">
					      
								     	 <thead>
			                                    <th>Fichero</th>
			                                   
			                                    
			                            </thead>
			                             <tbody>
                                      
								            <%
								            for(MediaObject docMedia: factura.getFicheros()){
								            	String rutaBorra="/digitalDoc?op=borraDoc&id=" + usuario.getId() + "&idFactura=" + factura.getId()+"&fileName="+docMedia.getFilename() ;
								            	String fileName=docMedia.getFilename();
			                       				String key = docMedia.getDisplayURL();
			                       				if(!docMedia.getFilename().equals(factura.getFichPrincipal())){
								            %>
								            
								        	<tr>
								                <td><a target="_blank" href="<%=key%>"><%=fileName %></a></td>
								               	
								            </tr>
								        
								            <%
			                       				}
								            }
								            
								            %>
								        	
								            
								             </tbody>
                			  
								        
			                            </table>          
                                <br>
                            </div>
                         </div>
                    </div>
                    
                </div>
            </div>
        </div>


        <footer class="footer">
            <div class="container-fluid">
                <p class="copyright pull-right">
                    &copy; 2020 <a target="_blank" href="https://www.garceray.com">Garceray</a>
                </p>
            </div>
        </footer>

    </div>
</div>


</body>

    <!--   Core JS Files   -->
    <script src="assets/js/jquery-1.10.2.js" type="text/javascript"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>

	<!--  Checkbox, Radio & Switch Plugins -->
	<script src="assets/js/bootstrap-checkbox-radio-switch.js"></script>

	<!--  Charts Plugin -->
	<script src="assets/js/chartist.min.js"></script>

    <!--  Notifications Plugin    -->
    <script src="assets/js/bootstrap-notify.js"></script>

    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

    <!-- Light Bootstrap Table Core javascript and methods for Demo purpose -->
	<script src="assets/js/light-bootstrap-dashboard.js"></script>

	<!-- Light Bootstrap Table DEMO methods, don't include it in your project! -->
	<script src="assets/js/demo.js"></script>

</html>
