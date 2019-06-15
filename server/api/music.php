<?php
require './header.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $name = str_replace('"', '', $_POST['name']);
  $album = str_replace('"', '', $_POST['album']);
  $penyanyi = str_replace('"', '', $_POST['penyanyi']);
  $tahun = str_replace('"', '', $_POST['tahun']);
  
  $imgName = $_FILES['image']['name'];
  $imageTempPath = $_FILES['image']['tmp_name'];
  $fileName = $_FILES['files']['name'];
  $fileTempPath = $_FILES['files']['tmp_name'];

  $uploadDir = 'files/';
  $filePath = $uploadDir.uniqid().'-'.$fileName;
  $fileRealPath = '../'.$filePath;
  
  $imagePath = $uploadDir.uniqid().'-'.$imgName;
  $imageRealPath = '../'.$imagePath;

  $isSuccess = move_uploaded_file($fileTempPath, $fileRealPath);
  $isSuccess2 = move_uploaded_file($imageTempPath, $imageRealPath);
  if (isSuccess && isSuccess2) {
    if ($connection->query("INSERT INTO mobile2(name, album, penyanyi, tahun, imgUrl, url) VALUES ('$name', '$album', '$penyanyi', '$tahun', '$imagePath', '$filePath')")) {
      http_response_code(200);
      echo json_encode([
        'idMusic' => $connection->insert_id,
        'name' => $name,
        'album'=> $album,
        'penyanyi' => $penyanyi,
        'tahun' => $tahun,
        'imgUrl' => $imagePath,
        'url' => $filePath,
        ]);
      die();
    }
    echo json_encode(['msg' => 'fail on save db']);
    http_response_code(500);
    die();
  }
  $connection->close();
  echo json_encode(['msg' => 'fail on save data']);
  http_response_code(500);
  die();
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
  $query = $connection->query("SELECT * FROM mobile2 ORDER BY idMusic DESC");
  $blog = [];
  while ($data = $query->fetch_assoc()) {
	  $blog[] = $data;
  }
  echo json_encode(['music' => $blog]);
  $connection->close();
  die();
}

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
  $deletVar = [];
  parse_str(file_get_contents("php://input"),$deletVar);
  $idMusic = $deletVar['idMusic'];
  $query = "DELETE FROM mobile2 WHERE idMusic='$idMusic'";
  if($connection->query($query)) {
    http_response_code(200);
    die();
  }

  http_response_code(500);
  $connection->close();
  die();
}

if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
  $putVar = [];
  parse_str(file_get_contents("php://input"),$putVar);
  $idMusic = $putVar['idMusic'];
  $name = $putVar['name'];
  $album = $putVar['album'];
  $penyanyi = $putVar['penyanyi'];
  $tahun = $putVar['tahun'];
  
  $query = "UPDATE mobile2 SET name='$name', album='$album', penyanyi='$penyanyi', tahun='$tahun' WHERE idMusic='$idMusic'";
  if($connection->query($query)) {
    http_response_code(200);
    die();
  }

  http_response_code(500);
  $connection->close();
  die();
}
