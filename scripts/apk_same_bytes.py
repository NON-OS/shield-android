#!/usr/bin/env python3
"""Prove a signed package is an unsigned package plus an APK Signing Block, and nothing else.

Usage: scripts/apk_same_bytes.py <signed.apk> <unsigned.apk>
"""
import struct
import sys

MAGIC = b"APK Sig Block 42"
KNOWN = {0x7109871A: "v2", 0xF05368C0: "v3", 0x1B93AD61: "v3.1", 0x42726577: "padding"}


def eocd(data):
    at = data.rfind(b"PK\x05\x06", max(0, len(data) - 65557))
    if at < 0:
        sys.exit("no end of central directory record")
    size, offset, comment = struct.unpack_from("<IIH", data, at + 12)
    if offset == 0xFFFFFFFF or at + 22 + comment != len(data) or offset + size != at:
        sys.exit("an unexpected zip layout")
    return at, size, offset


def pairs(block):
    found, at = [], 8
    while at < len(block) - 24:
        length, ident = struct.unpack_from("<QI", block, at)
        found.append(ident)
        at += 8 + length
    if at != len(block) - 24:
        sys.exit("the signing block does not parse")
    return found


def main(signed_path, unsigned_path):
    signed = open(signed_path, "rb").read()
    unsigned = open(unsigned_path, "rb").read()
    s_end, s_size, s_cd = eocd(signed)
    u_end, u_size, u_cd = eocd(unsigned)
    if unsigned[u_cd - 16:u_cd] == MAGIC:
        sys.exit("the unsigned package is already signed")
    if signed[s_cd - 16:s_cd] != MAGIC:
        sys.exit("the signed package carries no APK Signing Block")
    (footer,) = struct.unpack_from("<Q", signed, s_cd - 24)
    start = s_cd - footer - 8
    if struct.unpack_from("<Q", signed, start)[0] != footer:
        sys.exit("the signing block does not parse")
    pad = start - u_cd
    if pad < 0 or pad >= 4096 or (pad and start % 4096) or any(signed[u_cd:start]):
        sys.exit("something other than zero padding sits between the entries and the signature")
    if signed[:u_cd] != unsigned[:u_cd]:
        sys.exit("the entries differ from the build")
    if s_size != u_size or signed[s_cd:s_end] != unsigned[u_cd:u_end]:
        sys.exit("the central directory differs from the build")
    patched = signed[s_end:s_end + 16] + struct.pack("<I", u_cd) + signed[s_end + 20:]
    if patched != unsigned[u_end:]:
        sys.exit("the end record differs from the build in more than its offset")
    kinds = [KNOWN.get(i) for i in pairs(signed[start:s_cd])]
    if None in kinds:
        sys.exit("the signing block carries something that is not a signature")
    print(f"same bytes: the build, {pad} bytes of zero padding and a signing block of "
          f"{footer + 8} bytes holding " + ", ".join(kinds))


if __name__ == "__main__":
    if len(sys.argv) != 3:
        sys.exit(__doc__)
    main(sys.argv[1], sys.argv[2])
