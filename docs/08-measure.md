# Measuring a proof on a phone

How to time a proof and read its peak memory on a real Android phone, and what to send to the
prover team. The rule for a published time is the median of three runs whose proofs the live
verifier accepts, with the device, the build and the thermal state.

## What the app measures

Settings, Time a proof on this phone, proves one real transfer on the v2 circuit at shape A with
every core of the phone, verifies it on the phone inside the time, and shows the result. The card
it leaves holds:

| Line | What it is |
|---|---|
| `proved` | the time of the proof, verification included |
| `built` | the time to build the witness before it |
| `peak` | the peak resident memory of the app process since it started, from `VmHWM` |
| `where` | the model, Android release, architecture, screen and RAM of the phone |

The proof needs no network, so airplane mode is fine and keeps the run quiet.

## Steps

1. Install the release APK by [05-install.md](05-install.md). Create or restore a wallet.
2. Charge above half, turn battery saver off, let the phone cool for ten minutes, close every
   other app, and note the room temperature.
3. Force stop the app (Settings, Apps, NØNOS, Force stop), open it and unlock it, so the peak
   counts this run only.
4. Settings, Time a proof on this phone. Keep the screen on and touch nothing until the time shows.
5. Share the card, then Export the proof, and keep both files under the number of the run.
6. Repeat steps 3 to 5 for runs two and three.

The first run after installing also builds the periodic cache, so it is the slowest and the
hungriest. Its peak is the memory a 4 GB phone has to hold, and runs two and three reuse the cache.

## What goes to the prover team

One row for the measured table of the prover, in the public [NON-OS/STARKs](https://github.com/NON-OS/STARKs) repository, and the three cards:

```text
| <model>, Android <release>, <RAM> GB RAM | <cores> | <median> s (runs <a>, <b>, <c>) | <peak of run one> GB |
```

Measure two phones: one with 4 GB of RAM, and the fastest at hand. Until a kit for the v2 verifier
exists in the core (`ci/verify-kit` covers the launch pool only), the row says the proofs are kept
and not yet checked against the live verifier, and the time is not published as the gate.
