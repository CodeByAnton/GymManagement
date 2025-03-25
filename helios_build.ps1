$path_to_root = Get-Location
$path_to_backend_resources = "$path_to_root\backend\src\main\resources"
$path_to_frontend_build = "$path_to_root\frontend\build"

Set-Location frontend
npm install
npm run build

Set-Location $path_to_backend_resources

# Ensure the 'static' directory is fully removed
if (Test-Path -Path "static") {
    Remove-Item -Recurse -Force static
}

Set-Location $path_to_frontend_build

Copy-Item img -Destination "$path_to_backend_resources\static\img" -Recurse
Copy-Item index.html -Destination "$path_to_backend_resources\static"
Copy-Item static/css -Destination "$path_to_backend_resources\static\static\css" -Recurse
Copy-Item static/js -Destination "$path_to_backend_resources\static\static\js" -Recurse

Set-Location "$path_to_root/backend"

mvn clean package -DskipTests

Set-Location $path_to_root

Copy-Item "$path_to_root/backend/target/backend.jar" -Destination "$path_to_root"
