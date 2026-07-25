README
======

Este proyecto fue maquetado primeramente con gemma4 26b 64k y posteriormente
el diseño fue complementado y robustecido por el modelo qwen 3.6 35b.

De esta forma el flujo demuestra que con este flujo se pueden armar proyectos
bien estructurados, de esta forma no se sobresatura  el context window a cada modelo
en una sola sesion.

prompt de ejemplo:

prompt (maquetador/idea) rapida
gemma4: creame una landing page estilo fedora core linux colores morados y grises
estilo fedora core linux retro de los 2000s.

prompt 2  (madurar codigo/diseño)
qwen 3.6: complementa este diseño  estilo fedora core linux colores morados y grises
estilo fedora core linux retro de los 2000s.

author Anibal Gomez