#!/bin/bash
cd /home/kavia/workspace/code-generation/notenest-72804-89f83100/notes_app_ui_workspace/notes_app_ui
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

