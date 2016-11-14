Steps to run staffservice + plannerengine in docker:

1. Build PlannerEngine by command 
    ```sh
    mvn -Denforcer.skip clean install
    ```
    and put result war file plannerengine-0.0.1-SNAPSHOT.war file into ./plannerengine directory
2. Build StaffService by command
    ```sh    
    mvn -Pprod clean install
    ```
    and put result war file into ./staffservice directory
3. Run docker
    ```sh
    $ docker-compose up --build -d
    ```
