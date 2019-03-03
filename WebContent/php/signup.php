<?php 
	ob_start();
	session_start();
?>
<html>
<head>
	<meta charset="UTF-8">
	<title>Dang ky</title>
  <style>
	.button {
		background-color: #4CAF50;
		border: none;
		color: white;
		padding: 15px 32px;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 16px;
		margin: 4px 2px;
		cursor: pointer;
	}
  </style>
</head>

<body style="font-size:125%;font-family:Arial, Helvetica, sans-serif;">
  <h1>Dang ky</h1>
  <?php
	if(isset($_POST['submit']) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['email']) && !empty($_POST['name']))
	{
		$ch=curl_init();
		curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/ScoreView/rest/scoreview/sign-up");
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query(array('username' => $_POST['username'],
					'password' => $_POST['password'], 'email' => $_POST['email'], 'name' => $_POST['name']
		
					)));
		curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
		curl_setopt($ch,CURLOPT_FOLLOWLOCATION,true);
		$resp=curl_exec($ch);
		curl_close($ch);
		if($resp=="true"){
			$_SESSION['valid']=true;
			$_SESSION['timeout']=time();
			$_SESSION['username']=$_POST['username'];
			header("location: verifyOTP.php");
			die();
		}else{
			$msg="Account exist";
		}
	}
  ?>
  <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
	<input type="text" placeholder="Username" name="username" value=""/>
	<br/><br/>
	<input type="text" placeholder="Password" name="password" value=""/>
	<br/><br/>
	<input type="text" placeholder="Email" name="email" value=""/>
	<br/><br/>
	<input type="text" placeholder="Name" name="name" value=""/>
	<br/><br/>
	<input type="submit" name="submit" class="button" value="Submit"/>
	<br></br>
	<h4><?php echo $msg; ?></h4>
  </form>

</body>
</html>
