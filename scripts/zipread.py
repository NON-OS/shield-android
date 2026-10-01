#!/usr/bin/env python3
"""The unzip modes these scripts use, for a machine without unzip.

Usage: zipread.py -Z1 ZIP | -l ZIP | -p ZIP [PATTERN...] | [-q] [-o] ZIP -d DIR
"""
import fnmatch
import os
import sys
import zipfile


def extract(archive, target):
    for info in archive.infolist():
        path = archive.extract(info, target)
        mode = info.external_attr >> 16
        if mode and not info.is_dir():
            os.chmod(path, mode & 0o7777)


def main(args):
    flags = [a for a in args if a.startswith("-") and a != "-d"]
    rest = [a for a in args if not a.startswith("-")]
    if not rest:
        sys.exit(__doc__)
    with zipfile.ZipFile(rest[0]) as archive:
        names = archive.namelist()
        if "-Z1" in flags or "-l" in flags:
            print("\n".join(names))
        elif "-p" in flags:
            patterns = rest[1:] or ["*"]
            for name in names:
                if any(fnmatch.fnmatchcase(name, p) for p in patterns):
                    sys.stdout.buffer.write(archive.read(name))
        elif "-d" in args:
            extract(archive, args[args.index("-d") + 1])
        else:
            sys.exit(__doc__)


if __name__ == "__main__":
    main(sys.argv[1:])
