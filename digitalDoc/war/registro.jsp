<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="author" content="Raul Tribaldos"/>
<meta name="description" content="Garceray"/>
<meta name="keywords" content="Garceray, transporte, camiones"/>
<meta name="language" content="spanish"/>
<%@ page import="com.digitaldoc.model.*" %>
<%@ page import="java.util.*" %>
<%
	Usuario usuario = (Usuario) request.getAttribute("usuario");
	Usuario cliente = (Usuario) request.getAttribute("cliente");
	
	String empresa="";
	String cif="";
	String email="";
	String otrosEmails="";
	String nombre="";
	String telefono="";
	String direccion="";
	String password="";
	String provincia="";
	String poblacion="";
	String pais="";
	String cp="";
	String acreedor="";
	String empleado="";
	boolean edicion=false;
	
	if(cliente!=null){
		edicion=true;
		empresa=cliente.getEmpresa();
		cif=cliente.getCif();
		email= cliente.getEmail();
		otrosEmails= cliente.getOtrosEmails();
		if("null".equals(otrosEmails)){
			otrosEmails="";
		}
		nombre= cliente.getNombre();
		telefono= cliente.getTelefono();
		direccion = cliente.getDireccion();
		password= cliente.getPassword();
		provincia= cliente.getProvincia();
		poblacion= cliente.getPoblacion();
		pais = cliente.getPais();
		cp= cliente.getCp();
		if(cliente.isAcreedor()) acreedor="checked";
		if(cliente.isEmpleado()) empleado="checked";
	}
	
	System.out.println(String.valueOf(usuario.getId()));
%>

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
                    <a class="navbar-brand" href="#">Nuevo Usuario</a>
                </div>
                
            </div>
        </nav>


        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Datos</h4>
                            </div>
                            <div class="content">
                                <form action="/digitalDoc" method="post">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label>Empresa</label>
                                                <input type="text" class="form-control" placeholder="Empresa"  name="empresa" value="<%=empresa%>">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="cif">CIF</label>
                                                <input type="cif" class="form-control" placeholder="Cif" name="cif" value="<%=cif%>">
                                            </div>
                                    	</div>
									</div>
									<div class="row">
										<div class="col-md-12">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">Email</label>
                                                <input type="email" class="form-control" placeholder="Emails separados por comas" name="email" value="<%=email%>">
                                            </div>
                                        </div>
									</div>
									<div class="row">
										<div class="col-md-12">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">Emails para env&iacute;os</label>
                                                <input type="otrosEmails" class="form-control" placeholder="Emails separados por comas" name="otrosEmails" value="<%=otrosEmails%>">
                                            </div>
                                        </div>
									</div>
									
                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label>Nombre</label>
                                                <input type="text" class="form-control" placeholder="Nombre y Apellidos" name="nombre" value="<%=nombre%>">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Telefono</label>
                                                <input type="text" class="form-control" placeholder="Tel&eacute;fono" name="telefono" value="<%=telefono%>">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label>Direccion</label>
                                                <input type="text" class="form-control" placeholder="Direcci&oacute;n" name="direccion" value="<%=direccion%>">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Password</label>
                                                <input type="text" class="form-control" placeholder="Password" name="password" value="<%=password%>">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Poblacion</label>
                                                <input type="text" class="form-control" placeholder="Poblaci&oacute;n" name="poblacion" value="<%=poblacion%>"> 
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Provincia</label>
                                                <input type="text" class="form-control" placeholder="Provincia" name="provincia" value="<%=provincia%>">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>CP</label>
                                                <input type="number" class="form-control" placeholder="CP" name="cp"  value="<%=cp%>">
                                        	</div>
                                    	</div>
                                   </div> 	
                                   <div class="row">
										<div class="col-md-4">
                                        	<div class="form-group">
                                                <label>Pa&iacute;s</label>
                                                <input type="text" class="form-control" placeholder="Pa&iacute;s" name="pais"  value="<%=pais%>">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                        	<div class="form-group">
                                                <center><label>Acreedor</label></center>
                                                <input type="checkbox" name="acreedor" value="1" class="form-control" placeholder="Acreedor" <%=acreedor%>>
                                            </div>
                                        </div>
                                         <div class="col-md-4">
                                        	<div class="form-group">
                                                 <center><label>Empleado</label></center>
                                                <input type="checkbox" name="empleado" value="1" class="form-control" placeholder="Empleado" <%=empleado%>>
                                            </div>
                                         </div>
                                   </div>
                                   
                                   <br/>
                                     
                                  <%
                                  	if(edicion){
                                   %>  
										<button type="submit" class="btn btn-info btn-fill pull-right">Guardar</button>
										<input type="hidden" name="op" value="actualizaCliente"/>
										<input type="hidden" name="idCliente" value="<%=cliente.getId()%>"/>
									<%
                                  	}else{
									%>
										<button type="submit" class="btn btn-info btn-fill pull-right">Registrar</button>
										<input type="hidden" name="op" value="nuevoCliente"/>
									<%
                                  	}
									%>
									<input type="hidden" name="id" value="<%=usuario.getId()%>"/>
									
                                    <div class="clearfix"></div>
                                </form>
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
