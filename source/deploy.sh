#! /bin/sh
gradle clean build uploadArchives \
-x :cordwood-mobile:uploadArchives \
-x :cordwood-oauth2:uploadArchives