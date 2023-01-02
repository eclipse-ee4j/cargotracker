FROM payara/server-full:5.2022.4

COPY target/postgresql.jar /tmp
COPY target/cargo-tracker.war /tmp
COPY post-boot-commands.asadmin /opt/payara/config/
