
# ==========================================
# JavaFX Application - Build and Package
# ==========================================

$ErrorActionPreference = "Stop"

# ----- Configuration -----

$AppName = "F1ManagerPlanner24"
$MainClass = "app.ui.Launcher"
$JavaFXModule = "javafx.controls"
$CreateZip = $true

# ----- Project paths -----

$ProjectDir = $PSScriptRoot
$TargetDir = Join-Path $ProjectDir "target"
$PackageDir = Join-Path $TargetDir "package"
$JavaFXDir = Join-Path $TargetDir "javafx-modules"
$DistDir = Join-Path $ProjectDir "dist"
$AppImageDir = Join-Path $DistDir $AppName

Write-Host ""
Write-Host "===================================="
Write-Host " Building $AppName"
Write-Host "===================================="
Write-Host ""

# ----- Find jpackage -----

$JpackageCommand = Get-Command jpackage -ErrorAction SilentlyContinue

if ($JpackageCommand) {
    $Jpackage = $JpackageCommand.Source
}
else {
    # Find jpackage beside javac if it is not on PATH
    $JavacCommand = Get-Command javac -ErrorAction SilentlyContinue

    if (-not $JavacCommand) {
        throw "Cannot find javac. Make sure Oracle JDK 25 is installed."
    }

    $JdkBin = Split-Path $JavacCommand.Source
    $Jpackage = Join-Path $JdkBin "jpackage.exe"

    if (-not (Test-Path $Jpackage)) {
        throw "Cannot find jpackage.exe in $JdkBin"
    }
}

Write-Host "Using jpackage: $Jpackage"

# ----- Read Maven project information -----

[xml]$Pom = Get-Content (Join-Path $ProjectDir "pom.xml")

$Namespace = New-Object System.Xml.XmlNamespaceManager($Pom.NameTable)
$Namespace.AddNamespace("m", "http://maven.apache.org/POM/4.0.0")

$ArtifactId = $Pom.SelectSingleNode("/m:project/m:artifactId", $Namespace).InnerText
$Version = $Pom.SelectSingleNode("/m:project/m:version", $Namespace).InnerText

$JarName = "$ArtifactId-$Version.jar"
$JarPath = Join-Path $TargetDir $JarName

Write-Host "Application JAR: $JarName"

# ----- Clean previous builds -----

Write-Host ""
Write-Host "Cleaning previous builds..."

& (Join-Path $ProjectDir "mvnw.cmd") clean package

if ($LASTEXITCODE -ne 0) {
    throw "Maven build failed."
}

# ----- Copy JavaFX dependencies -----

Write-Host ""
Write-Host "Preparing JavaFX dependencies..."

New-Item -ItemType Directory -Force -Path $JavaFXDir | Out-Null

& (Join-Path $ProjectDir "mvnw.cmd") `
    org.apache.maven.plugins:maven-dependency-plugin:3.8.1:copy-dependencies `
    "-DoutputDirectory=$JavaFXDir" `
    "-DincludeScope=runtime"

if ($LASTEXITCODE -ne 0) {
    throw "Failed to copy JavaFX dependencies."
}

# ----- Prepare application JAR -----

if (-not (Test-Path $JarPath)) {
    throw "Application JAR not found: $JarPath"
}

New-Item -ItemType Directory -Force -Path $PackageDir | Out-Null
Copy-Item $JarPath $PackageDir -Force

# ----- Remove old distribution -----

if (Test-Path $DistDir) {
    Remove-Item $DistDir -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $DistDir | Out-Null

# ----- Create portable application -----

Write-Host ""
Write-Host "Creating portable application..."

& $Jpackage `
    --type app-image `
    --name $AppName `
    --input $PackageDir `
    --dest $DistDir `
    --main-jar $JarName `
    --main-class $MainClass `
    --module-path $JavaFXDir `
    --add-modules $JavaFXModule

if ($LASTEXITCODE -ne 0) {
    throw "jpackage failed."
}

# ----- Copy saves folder -----

Write-Host ""
Write-Host "Preparing saves folder..."

$SourceSaves = Join-Path $ProjectDir "saves"
$DestinationSaves = Join-Path $AppImageDir "saves"

if (Test-Path $SourceSaves) {
    Copy-Item $SourceSaves $DestinationSaves -Recurse -Force
}
else {
    New-Item -ItemType Directory -Force -Path $DestinationSaves | Out-Null
}

# ----- Create ZIP archive -----

if ($CreateZip) {
    Write-Host ""
    Write-Host "Creating ZIP archive..."

    $ZipPath = Join-Path $DistDir "$AppName.zip"

    Compress-Archive `
        -Path $AppImageDir `
        -DestinationPath $ZipPath `
        -CompressionLevel Optimal `
        -Force
}

# ----- Finished -----

Write-Host ""
Write-Host "===================================="
Write-Host " BUILD COMPLETED!"
Write-Host "===================================="
Write-Host ""

Write-Host "Application: $AppImageDir"

if ($CreateZip) {
    Write-Host "ZIP archive: $ZipPath"
}

Write-Host ""
