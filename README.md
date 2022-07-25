<h1 align="center">Kerminal</h1>

## Comandos

Todos os comandos aqui listados são 100% configuráveis no plugin.

### ➥ Administrativos

| Comando                            | Descrição                                                                                     | Permissão                       |
|------------------------------------|-------------------------------------------------------------------------------------------|-------------------------------------|
| /setspawn                      | Define a localidade do jogador como spawn                                                     |                kerminal.setspawn
| /gamemode                      | Altera seu modo de jogo atual                                                                 |                kerminal.gamemode
| /fly                     | Permite o executor voar no servidor                                                                |                kerminal.fly
| /enchant                     | Aplica um encantamento em um item                                                              |                kerminal.enchant
| /clearchat                     | Limpa todo o chat do servidor                                                                 |                kerminal.clearchat
| /clear                     | Esvazia o inventário do executor                                                                 |                kerminal.clear
| /craft                     | Abre uma Bancada de Trabalho pro jogador                                                                 |                kerminal.craft
| /feed                     |  Recupera fome do executor                                                                |                kerminal.feed
| /heal                     |  Recupera vida do executor                                                                |                kerminal.heal
| /hat                     |  Define o item da mão do executor como chapéu                                                                |                kerminal.hat
| /onlines                     |  Mostra quantos jogadores tem online no momento                                     |                kerminal.onlines
| /jogadores                     |  Mostra o nome de todos os jogadores online no momento                                     |                kerminal.jogador
| /infolag                     |  Exibe todas as informações do servidor relacionadas ao desempenho                                  |                kerminal.infolag
| /createkit                     |  Cria um novo kit utilizando o inventário atual do executor                                  |                kerminal.createkit

### ➥ Normais

| Comando                            | Descrição                                                                                     | Permissão                       |
|------------------------------------|-------------------------------------------------------------------------------------------|-------------------------------------|
| /spawn                      | Teleporta o executor para o spawn                                                                |                kerminal.spawn
| /tpa                     | Envia um pedido de teleporte do executor para outro jogador                                     |                kerminal.tpa
| /tpaccept                     | Aceita o teleporte de um jogador                                                                 |                kerminal.tpaccept
| /tpadeny                     | Recusa o teleporte de um jogador                                                                 |                kerminal.tpadeny
| /fly                     | Permite o executor voar no servidor                                                                |                kerminal.fly
| /craft                     | Abre uma Bancada de Trabalho pro executor                                                         |                kerminal.craft
| /hat                     |  Define o item da mão do executor como chapéu                                                                |                kerminal.hat
| /enderchest                     |  Abre o enderchest do executor                                                                |                kerminal.enderchest
| /divulgar                     |  Anuncia para todos do servidor uma divulgação                                                   |                kerminal.divulgar
| /light                     |  Define a luminosidade do executor no máximo                                    |                kerminal.light
| /trash                     |  Abre uma lixeira para o executor                                     |                kerminal.trash
| /home                     |  Teleporta o executor para suas casas salvas                                     |                kerminal.home
| /sethome                     |  Define a nova casa do executor                                   |                kerminal.sethome
| /delhome                     |  Remove a nova casa do executor                                   |                kerminal.delhome
| /listhomes                     |  Exibe todas as casas do executor                                   |                kerminal.listhomes
| /back                     |  Retorna para última localização salva do executor                                  |                kerminal.back
| /ping                     |  Exibe o ping do executor                                  |                kerminal.ping
| /slime                     |  Exibe se a chunk atual do executor é uma Slime Chunk                                  |                kerminal.slime
| /kit                     |  Recebe um kit de escolha do executor                                  |                kerminal.kit
| /listkits                     |  Exibe todos os kits criados no servidor                                  |                kerminal.listkits
| /titlep | Manda um title para um jogador especifico!| kerminal.titleplayer
|/title | Manda um title para todos os jogadores | kerminal.title
| /aviso | Manda um broadcast para todos os jogadores! | kerminal.broadcast
|/bigorna | Abre uma bigorna virtual | kerminal.anvil
| /tpall | Teleporta todos jogadores online para a posição do executor | kerminal.tpall

## Permissões

As permissões são configuráveis, mas por padrão todo comando será `kerminal.COMANDO`.

Há outros tipos de permissões que irei citar abaixo.

| Permissão                          | Descrição                                                        |
|------------------------------------|------------------------------------------------------------------|
| kerminal.blockedcommands.bypass  | Permite burlar o sistema de comandos bloqueados                    |              
| kerminal.enderchest.others                     | Permite mecher no enderchest de outros jogadores                |
| kerminal.keepxp                     | Permite o jogador continuar com sua experiência                |
| PERMISSAO_PADRAO.delay.bypass                     | Teleporta o jogador instantaneamente               |
| kerminal.kit.NOME_DO_KIT                     | Permissão para recolher um kit específico  
|kerminal.sign.repair | Permissao para reparar na placa
| kerminal.sign.repair.all && kerminal.repair | Reparar ALL na placa, (Precisa das duas permissões)

## Eventos Customizados

- `GamemodeChangeEvent` - Chamado na alteração do gamemode.
- `PlayerBackEvent` - Chamado ao executar o comando /back para retornar.
- `PlayerHomeEvent` - Chamado ao teleportar-se para uma Home.

## Configurações

### ➥ Principal <sub>[ config.yml ]</sub>

Vou apenas explicar a partes mais "diferentes" da configuração, pois o resto ja é de padrão de outros...

- Regeneração
```Java
Regeneration:
  Enabled: true          # Habilita ou desabilita a modificação da regeneração
  Delay: 20              # Tempo em ticks para preencher a vida
```

- Mundos em Ticks
```Java
Worlds:
  world:                 # Nome do mundo para a definição
    Tick: 60             # Quantidade da velocidade dos ticks do mundo
```

### ➥ Entidades <sub>[ entities.yml ]</sub>

Você pode alterar as vidas e os drops de entidades vivas (living entities).

```Java
PIG:                       # Nome do tipo da entidade, no caso 'PIG'
  Health: 50               # Quantidade da vida da entidade
  Drops:                   # Configuração de todos os drops novos
    ExampleItem:
      chance: 80
      quantity: 1
      material: DIRT
      data: 0
      name: "&cExample"
      lore:
        - "&7Example"
      enchantments:         # Formato: ENCHANT;LEVEL
        - "DAMAGE_ALL;5"
```

### ➥ Alterar comandos <sub>[ commands.yml ]</sub>

Você pode mudar o comando principal, aliases, permissão, habilitar ou desabilitar cada um dos comandos existentes.

```Java
Spawn:                           # Não mecher... é a identificação do comando
  enabled: true                  # Ativa ou desativa o comando    
  command: "spawn"               # Nome do comando principal
  permission: kerminal.spawn     # Permissão do comando
  async: false                   # Definir comando como ASYNC (Alguns comandos podem não funcionar)
  aliases:                       # Definir as aliases (Caso não queira nenhuma, deixe {} no lugar)
    - "hub"
    - "lobby"
```

### ➥ Criar novos comandos <sub>[ customCommands.yml ]</sub>

Com o Kerminal, você tem a opção de criar novos comandos da sua maneira.

```Java
COMANDO_AQUI:
  Sound: NOTE_PLING
  Delay: 5
  MessageDelay: "&cAguarde %time% para usar o comando novamente."
  Message:
    - "&eVocê executou um comando de exemplo."
```

# Bstats

<strong>bStats</strong> é um serviço de estatísticas de código aberto para o software Minecraft que é utilizado pelo Kerminal para coletar estatísticas do servidor.

![](https://bstats.org/signatures/bukkit/Kerminal.svg)

**👋 Um agradecimento especial.**

_"Agradeço a todos que estão ou acreditam neste projeto, logo abaixo tem alguns nomes que ajudaram com ideias e correções de códigos."_

@iDimaBR, @ferraribr, @Kepe, @SrEdu0202, @BadNotice_

# Desenvolvedores
![ferraribr](https://minotar.net/armor/bust/ferraribr_/190.png)![idimabr](https://minotar.net/armor/bust/Idimabr/190.png)