# Stage 1: Build frontend
FROM node:14 AS frontend-builder

WORKDIR /app/frontend

# Copy frontend source code
COPY frontend/package*.json ./
RUN npm install
COPY frontend .

# Build frontend
RUN npm run build

# Stage 2: Build backend
FROM maven:3.8.4-eclipse-temurin-17 AS backend-builder

WORKDIR /app/backend

# Copy backend source code
COPY backend/pom.xml ./
COPY backend/src ./src

# Copy frontend build files to backend resources
COPY --from=frontend-builder /app/frontend/build /app/frontend/build

# Ensure the 'static' directory is fully removed
RUN rm -rf src/main/resources/static || true

# Create necessary directories and copy frontend build files to backend resources
RUN mkdir -p src/main/resources/static/img && \
    mkdir -p src/main/resources/static/css && \
    mkdir -p src/main/resources/static/js && \
    cp -r /app/frontend/build/img/* src/main/resources/static/img && \
    cp /app/frontend/build/index.html src/main/resources/static && \
    cp -r /app/frontend/build/static/css/* src/main/resources/static/css && \
    cp -r /app/frontend/build/static/js/* src/main/resources/static/js

# Build backend
RUN mvn clean package -DskipTests

# Stage 3: Copy backend.jar to host
FROM scratch AS export

# Copy the built jar file to the root of the project
COPY --from=backend-builder /app/backend/target/backend.jar ./backend.jar