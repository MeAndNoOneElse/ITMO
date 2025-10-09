#!/bin/bash

# Script to compile and run FastCGI server for Lab 1
# Area Check Application

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$SCRIPT_DIR/src"
LIB_DIR="$SCRIPT_DIR/lib"
BUILD_DIR="$SCRIPT_DIR/build"
LOG_DIR="$SCRIPT_DIR/logs"
SOCKET_FILE="$SCRIPT_DIR/fcgi.sock"
FASTCGI_JAR="$LIB_DIR/fastcgi-lib.jar"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored messages
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check if FastCGI library exists
if [ ! -f "$FASTCGI_JAR" ]; then
    print_error "FastCGI library not found at $FASTCGI_JAR"
    print_info "Please download fastcgi-lib.jar and place it in the lib/ directory"
    print_info "You can download it from: https://repo1.maven.org/maven2/com/fastcgi/fastcgi-lib/2.1/fastcgi-lib-2.1.jar"
    exit 1
fi

# Create necessary directories
mkdir -p "$BUILD_DIR"
mkdir -p "$LOG_DIR"

# Remove old socket file if exists
if [ -S "$SOCKET_FILE" ]; then
    print_info "Removing old socket file..."
    rm -f "$SOCKET_FILE"
fi

# Compile Java source
print_info "Compiling Java sources..."
javac -cp "$FASTCGI_JAR" -d "$BUILD_DIR" "$SRC_DIR"/*.java

if [ $? -ne 0 ]; then
    print_error "Compilation failed!"
    exit 1
fi

print_info "Compilation successful!"

# Start FastCGI server
print_info "Starting FastCGI server..."
print_info "Socket: $SOCKET_FILE"
print_info "Press Ctrl+C to stop the server"

# Run the server
cd "$BUILD_DIR" || exit 1
java -cp ".:$FASTCGI_JAR" -DFCGI_PORT="$SOCKET_FILE" RequestHandler

# Cleanup on exit
if [ -S "$SOCKET_FILE" ]; then
    print_info "Cleaning up socket file..."
    rm -f "$SOCKET_FILE"
fi
