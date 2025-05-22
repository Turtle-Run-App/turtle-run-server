package com.turtleRun.be.player.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerCharacterId;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private Integer level;

    private Long exp;

    private Long attackStat;
}
