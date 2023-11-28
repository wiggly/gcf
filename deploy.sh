#!/bin/sh

rm -rf deploy/*

sbt "clean; assembly" && cp target/scala-2.13/wiggly.jar deploy

gcloud functions deploy wiggly \
  --gen2 \
  --entry-point=wiggly.CloudFunction \
  --runtime=java11 \
  --trigger-http \
  --memory=512MB \
  --allow-unauthenticated \
  --project=data-tooling-nonprod \
  --region=europe-west2 \
  --source=$PWD/deploy
