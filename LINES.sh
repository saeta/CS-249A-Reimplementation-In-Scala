#!/bin/sh

find . -print | grep -e .scala | xargs -J {} wc -l {}
