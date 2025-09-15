# Fin de journée

Application Android (Kotlin + Jetpack Compose, Material 3) :
- Saisis l'heure d'arrivée
- Calcule l'heure de départ = arrivée + **8h27**
- Mémorise la dernière heure (DataStore)
- Icône adaptive inspirée DGFiP (bleu + or)

## Build rapide (Android Studio)
1. Ouvrir le projet
2. **Build > Build APK(s)**
3. Récupérer `app/build/outputs/apk/debug/app-debug.apk`

## Ligne de commande
```bash
./gradlew assembleDebug   # si le wrapper est configuré sur ta machine
```
