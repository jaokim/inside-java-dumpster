#!/bin/bash
# This script prepares the database, i.e. creates the tables and fills it with
# data. Its supposed to be run once, generate database import files which can
# be included in the Dockerfile.

java -cp DatabaseServer.jar inside.dumpster.database.DatabaseGenerator jdbc:derby:dumpster;create=true


