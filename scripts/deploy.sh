#!/bin/bash

# ETL Framework Deployment Script

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# Default values
DEFAULT_TARGET_DIR="/opt/etl-framework"
DEFAULT_ENVIRONMENT="dev"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --target-dir)
            TARGET_DIR="$2"
            shift 2
            ;;
        --environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  --target-dir DIR        Target deployment directory (default: $DEFAULT_TARGET_DIR)"
            echo "  --environment ENV       Environment (dev/staging/prod, default: $DEFAULT_ENVIRONMENT)"
            echo "  --help, -h              Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

TARGET_DIR="${TARGET_DIR:-$DEFAULT_TARGET_DIR}"
ENVIRONMENT="${ENVIRONMENT:-$DEFAULT_ENVIRONMENT}"

echo "=== ETL Framework Deployment ==="
echo "Target directory: $TARGET_DIR"
echo "Environment: $ENVIRONMENT"

# Create target directory
sudo mkdir -p "$TARGET_DIR"

# Copy distribution files
echo "Copying distribution files..."
sudo cp -r "${PROJECT_DIR}/dist/"* "$TARGET_DIR/"

# Set permissions
sudo chown -R $(whoami):$(id -gn) "$TARGET_DIR"
find "$TARGET_DIR" -name "*.sh" -exec chmod +x {} \;

echo "=== Deployment completed successfully ==="
echo "ETL Framework deployed to: $TARGET_DIR"
echo "Job scripts are ready to use!"
