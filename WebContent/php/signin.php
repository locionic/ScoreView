<?php 
	ob_start();
	session_start();
?>

 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<?php
		if(!empty($_SESSION)){
			header("refresh: 5; url=ci.php");
			echo 'User has been signed in. Redirecting to portal...;';
			die();
		}
		$msg='';
		if(isset($_POST['login'])&&!empty($_POST['username'])&&!empty($_POST['password'])){
				$ch=curl_init();
				curl_setopt($ch, CURLOPT_URL,"http://localhost:8080/ScoreView/rest/scoreview/sign-in");
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query(array('username' => $_POST['username'],
					'password' => $_POST['password'])));
				//Receive server's response
				curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
				$server_output = curl_exec($ch);
				curl_close($ch);
				if($server_output=="true"){
					$_SESSION['valid'] = true;
					$_SESSION['timeout'] = time();
					$_SESSION['username']=$_POST['username'];
					header("location: ci.php");
					die();
					
				}
				else{
					$msg = 'You have entered an invalid username or password!';
				}
		}
				
	?>
	<div class="login-page">
  		<div class="form">   		
    		<form class="login-form" method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>">
      			<p>
					Username: <input type="text" placeholder="username" name="username" required autofocus/>
				</p>
				
      			<p>
					Password: <input type="password" placeholder="password" name="password" required/>
				</p>
      			<input type="submit" name="login"></input>
				<h4>
					<?php echo $msg; ?>
				</h4>
    		</form>
 		 </div>
	</div>
</body>
</html>