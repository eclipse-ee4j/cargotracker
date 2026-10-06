FROM payara/server-full:7.2026.9

COPY target/postgresql.jar /tmp
COPY target/cargo-tracker.war /tmp
COPY post-boot-commands.asadmin /opt/payara/config/
