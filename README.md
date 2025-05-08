# Employee Management Application

This is a Spring Boot application for employee management with CI/CD pipeline integration.

## CI/CD Pipeline

This project includes a GitHub Actions CI/CD pipeline that automates the build, test, and deployment processes.

### Pipeline Features

- **Continuous Integration**: Automatically builds and tests the application on every push to main/master branch and pull requests.
- **Artifact Management**: Uploads the built JAR file as an artifact for later use.
- **Deployment Options**: Includes commented deployment configurations for:
  - Server deployment via SSH
  - Heroku deployment

### How to Use the CI/CD Pipeline

1. **Push your code**: The pipeline automatically triggers on pushes to main/master branches.
2. **Manual Trigger**: You can manually trigger the workflow from the GitHub Actions tab.
3. **Deployment Configuration**:
   - For server deployment: Uncomment the SSH deployment section and add your server credentials as GitHub secrets.
   - For Heroku deployment: Uncomment the Heroku section and add your Heroku credentials as GitHub secrets.

### Required Secrets for Deployment

For SSH deployment:
- `HOST`: Your server hostname or IP
- `USERNAME`: SSH username
- `SSH_PRIVATE_KEY`: SSH private key for authentication

For Heroku deployment:
- `HEROKU_API_KEY`: Your Heroku API key
- `HEROKU_APP_NAME`: Your Heroku application name
- `HEROKU_EMAIL`: Your Heroku account email

## Docker Support

The application includes Docker support for containerized deployment:

- `Dockerfile`: Defines how to build the application container
- `docker-compose.yml`: Simplifies running the containerized application

### Running with Docker

```bash
# Build and start the application
docker-compose up -d

# Stop the application
docker-compose down
```

## Development

This is a Maven project. You can build and run it locally:

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/*.jar
```
