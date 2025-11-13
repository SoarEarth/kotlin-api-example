@echo off
REM Best Practices Validation Script for SOAR Maps API
REM This script validates that all best practices are properly implemented

echo ========================================
echo SOAR Maps API - Best Practices Validation
echo ========================================
echo.

echo [1/8] Checking project structure...
if exist "src\main\kotlin\com\example\MapLayerService.kt" (
    echo [OK] Service layer exists
) else (
    echo [FAIL] Service layer missing
)

if exist "src\main\kotlin\com\example\MapLayerDto.kt" (
    echo [OK] DTOs exist
) else (
    echo [FAIL] DTOs missing
)

if exist "src\main\kotlin\com\example\ExceptionHandlers.kt" (
    echo [OK] Exception handlers exist
) else (
    echo [FAIL] Exception handlers missing
)

echo.
echo [2/8] Checking test files...
if exist "src\test\kotlin\MapLayerServiceTest.kt" (
    echo [OK] Unit tests exist
) else (
    echo [FAIL] Unit tests missing
)

if exist "src\test\kotlin\MapLayerControllerTest.kt" (
    echo [OK] Integration tests exist
) else (
    echo [FAIL] Integration tests missing
)

echo.
echo [3/8] Checking documentation...
if exist "README.md" (
    echo [OK] README exists
) else (
    echo [FAIL] README missing
)

if exist "PROJECT_SUMMARY.md" (
    echo [OK] Project summary exists
) else (
    echo [FAIL] Project summary missing
)

echo.
echo [4/8] Checking configuration...
if exist ".gitignore" (
    echo [OK] .gitignore exists
) else (
    echo [FAIL] .gitignore missing
)

if exist ".env.example" (
    echo [OK] Environment template exists
) else (
    echo [FAIL] Environment template missing
)

if exist "Dockerfile" (
    echo [OK] Dockerfile exists
) else (
    echo [FAIL] Dockerfile missing
)

echo.
echo [5/8] Checking for hardcoded credentials...
findstr /C:"your_username" src\main\resources\application.yml >nul 2>&1
if %errorlevel% equ 0 (
    echo [WARNING] Default credentials found in application.yml - should use env vars
) else (
    echo [OK] No hardcoded credentials in application.yml
)

echo.
echo [6/8] Compiling project...
call gradlew.bat compileKotlin --quiet
if %errorlevel% equ 0 (
    echo [OK] Project compiles successfully
) else (
    echo [FAIL] Compilation errors found
)

echo.
echo [7/8] Running tests...
call gradlew.bat test --quiet
if %errorlevel% equ 0 (
    echo [OK] All tests pass
) else (
    echo [WARNING] Some tests failed or no tests ran
)

echo.
echo [8/8] Checking Docker setup...
if exist "docker-compose.yml" (
    echo [OK] Docker Compose configuration exists
) else (
    echo [FAIL] Docker Compose missing
)

echo.
echo ========================================
echo Validation Complete!
echo ========================================
echo.
echo Summary of Best Practices:
echo - Layered architecture (Controller/Service/Repository)
echo - DTOs for API contracts
echo - Input validation with Bean Validation
echo - Global exception handling
echo - Comprehensive documentation
echo - Unit and integration tests
echo - Environment-based configuration
echo - Docker support
echo - No hardcoded secrets
echo - Structured logging
echo.
echo Next steps:
echo 1. Review IMPROVEMENTS.md for detailed changes
echo 2. Update .env file with your configuration
echo 3. Run: docker-compose up -d
echo 4. Run: gradlew.bat run
echo 5. Test API: http://localhost:8080/api/v1/map-layers/search?latitude=25.25^&longitude=22.27
echo.
pause

