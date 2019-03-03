<?php
	session_start();
	if(empty($_SESSION)){
		header("location: signup.php");
		die();
	}
?>
<html>
<head>
	<meta charset="UTF-8">
	<title>Verify OTP code</title>
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
  <h1>Verify otp code by user: <?php echo $_SESSION['username']; ?> </h1>
  <?php
	$resp="";
	$msg="";
	if(isset($_POST['submit']) and !empty($_POST['otpcode']))
	{
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/ScoreView/rest/scoreview/verifyOTP/" .$_POST['otpcode']);
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
		$resp = curl_exec($ch);
		curl_close($ch);
	}
	if($resp="Success verify otp" && !empty($_POST['otpcode'])){
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/ScoreView/rest/scoreview/verifyOTP/".$_POST['otpcode']."/".$_SESSION['username']);
		curl_setopt($ch, CURLOPT_POST, 1);	
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
		$resp = curl_exec($ch);
		curl_close($ch);
		$msg = " Because you already verify";
	}
  ?>
  <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
	
	<input type="text" placeholder="OTP Code" name="otpcode" value=""/>
	<br/><br/>
	<input type="submit" name="submit" class="button" value="Submit"/>
	<br></br>
	<h4><?php echo $resp; echo $msg;?></h4>
  </form>

</body>
</html>
