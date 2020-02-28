use gerenciador_salas;
SET @@global.event_scheduler = 1;
SET SQL_SAFE_UPDATES = 0;
delimiter |
CREATE EVENT verificar_reservas_ativas
    ON SCHEDULE
        EVERY 10 MINUTE
        STARTS CURDATE()
        ON COMPLETION PRESERVE ENABLE DO
            BEGIN
				UPDATE gerenciador_salas.alocacao_sala SET ativo = 0 WHERE dataHoraFim < NOW();
            END 
| delimiter ;