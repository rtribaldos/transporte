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
<%@ page import="com.digitaldoc.dao.*" %>


<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	List<Usuario> listUsuarios = (List<Usuario>) request.getAttribute("listUsuarios");
	List<Documento> listFacturas = (List<Documento>) request.getAttribute("listFacturas");
	Usuario usuario = (Usuario) request.getAttribute("usuario");
		
	Cancelacion cancelacion = (Cancelacion) request.getAttribute("cancelacion");
	String urlUpload= blobstoreService.createUploadUrl("/upload");
	
	String styleDivDocs="display: block; visibility: visible;";
	String idCancelacion="0";
	Calendar cal= Calendar.getInstance();
	String fecha= df.format(cal.getTime());
	String vencimiento="";
	String factura="";
	String referencia="";
	String importe="0";
	String importeDivisas="0";
	String nomFichero="";
	
	Usuario cliente=null;
	String idEmpresa="";
	String habilitado="";
	String tipo="nuevaCancelacion";
	String key="";
	
	if (cancelacion==null){
		styleDivDocs="display: none; visibility: hidden;";
	}else{
		idCancelacion = cancelacion.getId();
		tipo= "editaCancelacion";
		if(cancelacion.getFechaAnticipo() != null){
			fecha = df.format(cancelacion.getFechaAnticipo());
		}
		if(cancelacion.getVencimiento() != null){
			vencimiento = df.format(cancelacion.getVencimiento());
		}
		if(cancelacion.getFactura() != null && !"".equals(cancelacion.getFactura())){
			factura = cancelacion.getFactura();
		}
		if(cancelacion.getReferencia() != null && !"".equals(cancelacion.getReferencia())){
			referencia = cancelacion.getReferencia();
		}
		if(cancelacion.getImporte() != null){
			importe = String.valueOf(cancelacion.getImporte());
		}
		if(cancelacion.getImporteDivisas() != null){
			importeDivisas = String.valueOf(cancelacion.getImporteDivisas());
		}
		if(cancelacion.getFichero() != null && cancelacion.getFichero().getFilename() != null){
			nomFichero=cancelacion.getFichero().getFilename();
			key = cancelacion.getFichero().getDisplayURL();
		}
		cliente = UsuarioDAO.getUsuarioByMail(PMF.get().getPersistenceManager(), cancelacion.getCliente().getEmail());
		idEmpresa= String.valueOf(cliente.getId());
	}
	
	if(!usuario.getPerfil().equals("SuperAdmin")){
		habilitado="readonly";
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
			 $( "#datepicker2" ).datepicker();
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
                <li>
                    <a href="/digitalDoc?op=clientes&id=<%=usuario.getId()%>">
                        <i class="pe-7s-user"></i>
                        <p>Usuarios</p>
                    </a>
                </li>
                 <li>
                 			<a href="/digitalDoc?op=facturas&id=<%=usuario.getId()%>">
                     		<i class="pe-7s-news-paper"></i>
                     		<p>Facturas</p>
                	 		</a>
           		</li>
           		<li>
                 		<a href="/digitalDoc?op=anticipos&id=<%=usuario.getId()%>">
                     		<i class="pe-7s-news-paper"></i>
                     		<p>Anticipos</p>
               	 		</a>
           		</li>
           		<li class="active">
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
                    <a class="navbar-brand" href="#">Cancelaci&oacute;n</a>
                </div>
                
            </div>
        </nav>

        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Datos de la cancelaci&oacute;n</h4>
                            </div>
                            <div class="content">
                            
                              
                              <form action="<%= urlUpload %>" method="post" enctype="multipart/form-data">
                               		<div class="row">
                                        <div class="col-md-9">
                                            <div class="form-group">
                                                <label>Acreedor</label>
                                                <select class="form-control" placeholder="Empresa" required name="empresa" <%=habilitado%>>
                                                	
                                                <%
                                                	if(cliente != null){
                                                %>
                                                	   <option value=<%=cliente.getId()%>><%=cliente.getEmpresa()%></option>
                                                <%		
                                                	}else{
                                                	 for(Usuario empresa : listUsuarios){
                                                		if(empresa.getEmpresa() != null){
                                               	%>
                                               			<option value=<%=empresa.getId()%>><%=empresa.getEmpresa()%></option> 		
                                                <%		
                                                		}
                                                	 }
                                                	}
                                                %>
                                                
                                                </select>
                                                
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Fecha Anticipo</label>
                                                <input type="text" class="form-control" placeholder="Fecha" required  id="datepicker"                         
                                                name="fecha" value="<%=fecha%>" <%=habilitado%>>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Vencimiento</label>
                                                <input type="text" class="form-control" placeholder="vencimiento"  id="datepicker2"                         
                                                name="vencimiento" value="<%=vencimiento%>" <%=habilitado%>>
                                            </div>
                                        </div>
                                         <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Id</label>
                                                <input type="text" class="form-control" placeholder="idCancelacion" name="idCancelacion" required 
                                                	value="<%=idCancelacion%>" <%=habilitado%>>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Referencia</label>
                                                <input type="text" class="form-control" placeholder="referencia" name="referencia"  
                                                	value="<%=referencia%>" <%=habilitado%> required> 
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Importe </label>
                                                <input type="text" class="form-control" placeholder="importe" name="importe"  
                                                	value="<%=importe%>" required <%=habilitado%>>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    
                                    
                                    <div class="row" style="<%=styleDivDocs%>">
                                    	
                                       
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Factura</label>
                                                
                                                <select class="form-control" placeholder="factura"  name="factura" <%=habilitado%>>
                                                
                                                 <%
                                                	if(cancelacion != null && cancelacion.getFactura() != null){
                                                		String facCan=cancelacion.getFactura();
                                                		int posG = facCan.indexOf("_");
                                                		if(posG > -1){
                                                			facCan =  facCan.substring(0, posG);
                                                		}
                                                %>
                                                	   <option value=<%=cancelacion.getFactura() %>><%=facCan%></option>
                                                <%		
                                                	}
                                                String sFactura="";
                                                if(cancelacion != null){
                                                	for(Documento documento : listFacturas){
                                                		String sFac = documento.getId();
                                               			int posG = sFac.indexOf("_");
                                                   		if(posG > -1){
                                                   			sFactura =  sFac.substring(0, posG);
                                                   		}
                                            				
                                                			
                                            	%>
                                           			<option value="<%=sFac%>"><%=sFactura%></option> 		
                                            	<%		
                                            		
                                                			
                                               			
                                                	}
                                                }
                                            	%>
                                            	</select>
                                            
                                            </div>
                                        </div>
                                    	                                        
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Imp. Divisas</label>
                                                <input type="text" class="form-control" placeholder="divisas" name="divisas"  
                                                	value="<%=importeDivisas%>" <%=habilitado%>>
                                            </div>
                                        </div>
                                        
                                    </div>
                                    
                                    <div class="row" style="<%=styleDivDocs%>">
                                    	
                                        
                                        <div class="col-md-6">
	                                    	<div class="form-group">
	                                      		<label for="fichero"><a target="_blank" href="<%=key%>">Fichero >> <%=nomFichero%></a></label>
	                                      		<input type="file" name="fichero" <%=habilitado%>>
	                                  		</div>
	                                  	</div>
                                    
                                    </div>
                                    
									
									<input type="hidden" name="op" value="<%=tipo%>"/>
									<input type="hidden" name="id" value="<%=usuario.getId()%>"/>
									<input type="hidden" name="idCancelacion" value="<%=idCancelacion%>"/>
									<button type="submit" class="btn btn-info btn-fill pull-right" <%=habilitado%>>Guardar datos</button>
                                    <div class="clearfix"></div>
                                </form>
                                
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

   
</html>
