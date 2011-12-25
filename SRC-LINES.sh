#!/bin/sh

find . -print | grep -e "\.scala$" | grep "\./src/" | xargs -J {} wc -l {}
