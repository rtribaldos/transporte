<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="author" content="Raul Tribaldos"/>
<meta name="description" content="Garceray"/>
<meta name="keywords" content="Garceray, transporte, camiones"/>
<meta name="language" content="spanish"/>

<%@ page import="com.digitaldoc.model.Usuario" %>
<%@ page import="java.util.*" %>
<%
	List<Usuario> listUsuarios = (List<Usuario>) request.getAttribute("listUsuarios");
	Usuario usuario = (Usuario) request.getAttribute("usuario");
	String tipoCliente = (String) request.getAttribute("tipoCliente");
	if(tipoCliente == null || "".equals(tipoCliente) || "null".equals(tipoCliente)){
		tipoCliente="Usuarios";
	}
%>
	
<title>Garceray</title>
	
	<link href="assets/css/smoothness/jquery-ui-1.10.4.custom.css" rel="stylesheet">
     
	<script src="assets/js/jquery-1.10.2.js"></script>
	<script src="assets/js/jquery-ui-1.10.4.custom.js"></script>
	<script type="text/javascript" language="javascript" src="assets/js/jquery.dataTables.js"></script>
	<script src="assets/js/jquery.validate.min.js"></script>

	<!-- Bootstrap core CSS     -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />

    <!-- Animation library for notifications   -->
    <link href="assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="assets/css/light-bootstrap-dashboard.css" rel="stylesheet"/>

    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />
    
	<link rel="stylesheet" href="assets/css/demo_table.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.22/css/jquery.dataTables.min.css">
    
    <script type="text/javascript" language="javascript" src="assets/js/tableInit.js"></script>

</head>
<body>

<div class="wrapper">
    <div class="sidebar" data-color="green" data-image="assets/img/garceray/logo_transparente.png">

    <!--

        Tip 1: you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple"
        Tip 2: you can also add an image using data-image tag

    -->

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
                 <li >
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
                    <button type="button" class="navbar-toggle" data-toggle="collapse">
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#"><%=tipoCliente%></a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-left">
                        <li class="dropdown">
                              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <i class="pe-7s-users"></i>
                                    <b class="caret"></b>
                              </a>
                              <ul class="dropdown-menu">
                                <li><a href="/digitalDoc?op=clientes&id=<%=usuario.getId()%>&tipoCliente=c">Clientes</a></li>
                                <li><a href="/digitalDoc?op=clientes&id=<%=usuario.getId()%>&tipoCliente=a">Acreedores</a></li>
                                <li><a href="/digitalDoc?op=clientes&id=<%=usuario.getId()%>&tipoCliente=e">Empleados</a></li>
                                 <li><a href="/digitalDoc?op=clientes&id=<%=usuario.getId()%>&tipoCliente=u">Todos</a></li>
                              </ul>
                        </li>
                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                    	<li class="dropdown">
                              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    Acciones
                                    <b class="caret"></b>
                              </a>
                              <ul class="dropdown-menu">
                                <li><a href="/digitalDoc?op=registro&id=<%=usuario.getId()%>">Nuevo Cliente</a></li>
                              </ul>
                        </li>
                    	<li>
                           <a href="/">
                               Salir
                            </a>
                        </li>
					</ul>
                </div>
            </div>
        </nav>


        <div class="content">
            <div class="container-fluid">
                <div class="row">
                		<table id="tabla" class="table table-hover" width="100%">
					        <thead>
					            <tr>
					                <th>Empresa</th>
					                <th>Nombre</th>
					                <th>Email</th>
					                <th>Tel&eacute;fono</th>
					                <th>Borrar</th>
					            </tr>
					        </thead>
					        <tfoot>
					            <tr>
					                <th>Empresa</th>
					                <th>Nombre</th>
					                <th>Email</th>
					                <th>Tel&eacute;fono</th>
					                <th>Borrar</th>
					            </tr>
					        </tfoot>
					        <tbody>
					        <%
					        	for(Usuario cliente: listUsuarios){
					        		String rutaBorra="/digitalDoc?op=borraCliente&id=" + usuario.getId() + "&idCliente=" + cliente.getId();
					        		String sEdit="/digitalDoc?op=ediCliente&id=" + usuario.getId()+ "&idCliente=" + cliente.getId();
					        		%>
					        		<tr>
					                	<td><a href="<%=sEdit%>"><%=cliente.getEmpresa()%></a></td>
					                	<td><a href="<%=sEdit%>"><%=cliente.getNombre()%></a></td>
					                	<td><%=cliente.getEmail()%></td>
					                	<td><%=cliente.getTelefono()%></td>
					                	<td><a href="<%=rutaBorra%>" class="confirmation"><i class="pe-7s-trash"></i></a></td>
                                    </tr>
					        		<%
					        	}
					        %>
					        </tbody>
    					</table>
				</div>
            </div>
        </div>


        <footer class="footer">
            <div class="container-fluid">
                <nav class="pull-left">
                    <ul>
                        <li>
                            <a href="#">
                                Principal
                            </a>
                        </li>

                    </ul>
                </nav>
                <p class="copyright pull-right">
                    &copy; 2020 <a target="_blank" href="https://www.garceray.com">Garceray</a>
                </p>
            </div>
        </footer>

    </div>
</div>


</body>

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
	
	<script type="text/javascript">
		    var elems = document.getElementsByClassName('confirmation');
		    var confirmIt = function (e) {
		        if (!confirm('¿Seguro que desea borrar el cliente?')) e.preventDefault();
		    };
		    for (var i = 0, l = elems.length; i < l; i++) {
		        elems[i].addEventListener('click', confirmIt, false);
		    }
		</script>

</html>

