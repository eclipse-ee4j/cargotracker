FROM payara/server-full:6.2023.12

COPY target/postgresql.jar /tmp
COPY target/cargo-tracker.war /tmp
COPY post-boot-commands.asadmin /opt/payara/config/
