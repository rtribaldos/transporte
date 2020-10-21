<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Garceray Aplication">
    <meta name="author" content="Ra&uacute;l Tribaldos">
    <meta name="keyword" content="Garceray, Transporte, Log&iacute;stica, Empresa, Logistica, Documentos digitales">
    <link rel="shortcut icon" href="assets/img/favicon.ico">

    <title>Garceray</title>

    <!-- Bootstrap CSS -->    
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="assets/css/bootstrap-theme.css" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="assets/css/elegant-icons-style.css" rel="stylesheet" />
    <link href="assets/css/font-awesome.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="assets/css/style.css" rel="stylesheet">
    <link href="assets/css/style-responsive.css" rel="stylesheet" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

  <body class="login-img3-body">
  
	<div class="container">
    
      <form class="login-form" action="/digitalDoc" method="POST">
      	              
        <div class="login-wrap">
            <p class="login-img"><i class="icon_lock_alt color-white"></i></p>
            <div class="input-group">
              <span class="input-group-addon"><i class="icon_profile"></i></span>
              <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Usuario" autofocus>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><i class="icon_key_alt"></i></span>
                <input type="password" class="form-control" id="password" name="password" placeholder="Contrase&ntilde;a">
            </div>
            <label class="checkbox">
               <span class="pull-right"> <a href="#"> Olvid&oacute; Contrase&ntilde;a?</a></span>
            </label>
            <button class="btn btn-warning btn-lg btn-block" type="submit">Entrar</button>
            <input type="hidden" id="op" value="login">
        </div>
        
      </form>

    </div>
  </body>
</html>
