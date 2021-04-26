FROM payara/server-full

COPY target/postgresql.jar /tmp
COPY target/cargo-tracker.war $DEPLOY_DIR

RUN echo 'add-library /tmp/postgresql.jar' > $POSTBOOT_COMMANDS