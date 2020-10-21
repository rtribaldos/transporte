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
<%@ page import="com.digitaldoc.dao.DocumentosDAO" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	List<Usuario> listUsuarios = (List<Usuario>) request.getAttribute("listUsuarios");
	List<Documento> listFacturas = (List<Documento>) request.getAttribute("listFacturas");
	Usuario usuario = (Usuario) request.getAttribute("usuario");
		
	Anticipo anticipo = (Anticipo) request.getAttribute("anticipo");
	String urlUpload= blobstoreService.createUploadUrl("/upload");
	
	String styleDivDocs="display: block; visibility: visible;";
	String idAnticipo="0";
	Calendar cal= Calendar.getInstance();
	String fecha= df.format(cal.getTime());
	Usuario acreedor=null;
	String idEmpresa="";
	String habilitado="";
	String tipoAnticipo="nuevoAnticipo";
	String key="";
	String nomFichAnticipo="";
 	
	if (anticipo==null){
		styleDivDocs="display: none; visibility: hidden;";
	}else{
		idAnticipo = anticipo.getId();
		tipoAnticipo= "editaAnticipo";
		if(anticipo.getFecha() != null){
			fecha = df.format(anticipo.getFecha());
		}
		habilitado="readonly";
		acreedor = anticipo.getAcreedor();
		idEmpresa= String.valueOf(acreedor.getId());
		if(anticipo.getFichero() != null){
			key=anticipo.getFichero().getDisplayURL(); 
			nomFichAnticipo=anticipo.getFichero().getFilename();
		}
	}
%>

	<title>Garceray</title>

	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />

	<script src="assets/js/jquery-1.10.2.js"></script>
	<script src="assets/js/jquery-ui-1.10.4.custom.js"></script>
		
	<!--   Core JS Files   -->
    
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
		
	<script>
		 $.datepicker.regional['es'] = {
			 closeText: 'Cerrar',
			 prevText: '<Ant',
			 nextText: 'Sig>',
			 currentText: 'Hoy',
			 monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
			 monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
			 dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
			 dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
			 dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],
			 weekHeader: 'Sm',
			 dateFormat: 'dd/mm/yy',
			 firstDay: 1,
			 isRTL: false,
			 showMonthAfterYear: false,
			 yearSuffix: ''
			 };
		$.datepicker.setDefaults($.datepicker.regional['es']);
		$(function () {
			 $( "#datepicker" ).datepicker();
		});
	</script>
	
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
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
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
                    <a class="navbar-brand" href="#">Nuevo anticipo</a>
                </div>
                
            </div>
        </nav>

        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Datos del anticipo</h4>
                            </div>
                            <div class="content">
                            
                              <form action="/digitalDoc" method="post">
                               		<div class="row">
                                        <div class="col-md-9">
                                            <div class="form-group">
                                                <label>Acreedor</label>
                                                <select class="form-control" placeholder="Empresa" required name="empresa" readOnly>
                                                	
                                                <%
                                                	if(acreedor != null){
                                                %>
                                                	   <option value=<%=acreedor.getId()%>><%=acreedor.getEmpresa()%></option>
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
                                                <input type="text" class="form-control" placeholder="Fecha" required  id="datepicker"                         
                                                name="fecha" value="<%=fecha%>" readOnly>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Id</label>
                                                <input type="text" class="form-control" placeholder="idAnticipo" name="idAnticipo" required value="<%=idAnticipo%>" 
                                                readOnly>
                                            </div>
                                        </div>
                                    	
                                    </div>
									
									<div class="clearfix"></div>
                                
                                </form>
                                
                                <br>
                                
                                 <div class="form-group">
                               		<label for="fichero2">Fichero de anticipo >> </label>
                             		<a target="_blank" href="<%=key%>"><%=nomFichAnticipo %></a>
                             	</div>
                                
                             
                             </div>
                             
                            
                             
                             <div class="header" style="<%=styleDivDocs%>">
                                <h4 class="title">Facturas</h4>
                             </div>
                            <div class="content" style="<%=styleDivDocs%>">
                                
                                   <table id="tabla" class="table table-hover" width="100%">
					      
								     	 <thead>
			                                    <th>Factura</th>
			                                    <th>Borrar</th>
			                                    
			                            </thead>
			                             <tbody>
                                      
								            <%
								            if(anticipo != null && anticipo.getFacturas() != null){
								             
								             for(String factura: anticipo.getFacturas()){
								            	 String rutaBorra="/digitalDoc?op=borraFacAnt&id=" + usuario.getId() + "&idAnticipo=" 
								             		+ anticipo.getId() + "&idFactura=" + factura ;
								            	 String url=DocumentosDAO.getUrlAnticipo(factura);
								            	 String urlEdit="/digitalDoc?op=ediFactura&id=" + usuario.getId() + "&idFactura=" + factura;
								            %>
								            
								        	<tr> 
								                <td><a target="_blank" href="<%=urlEdit%>" ><%=factura%></a></td>
								               	<td></td>
								            </tr>
								        
								            <%
								             }
								            }
								            
								            %>
								        	
								            
								             </tbody>
                			  
								        
			                            </table>   
			                            
			                            <br>
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
                    &copy; 2016 <a target="_blank" href="https://www.garceray.com">Garceray</a> Food & Liquid
                </p>
            </div>
        </footer>

    </div>
</div>


</body>

   
</html>
