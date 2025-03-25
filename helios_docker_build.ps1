docker build -t builder .

docker create --name temp-container --entrypoint " " builder

docker cp temp-container:/backend.jar ./backend.jar

docker rm temp-container