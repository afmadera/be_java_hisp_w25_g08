package com.grupo08.socialmeli.service;

import com.grupo08.socialmeli.dto.response.FollowDto;
import com.grupo08.socialmeli.dto.response.FollowersCountDto;
import com.grupo08.socialmeli.entity.Buyer;
import com.grupo08.socialmeli.entity.Seller;
import com.grupo08.socialmeli.entity.User;
import com.grupo08.socialmeli.exception.BadRequestException;
import com.grupo08.socialmeli.exception.NotFoundException;
import com.grupo08.socialmeli.repository.IBuyerRepository;
import com.grupo08.socialmeli.repository.ISellerRepository;
import com.sun.jdi.IntegerValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    IBuyerRepository buyerRepository;
    ISellerRepository sellerRepository;

    public UserServiceImpl(IBuyerRepository buyerRepository, ISellerRepository sellerRepository) {
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public FollowDto follow(int idBuyer, int idSeller) {

        Optional<Buyer> buyer = buyerRepository.findById(idBuyer);

        Optional<Seller> seller = sellerRepository.findById(idSeller);

        if(buyer.isEmpty()) throw new NotFoundException("No se encuentra comprador con ese ID.");

        if(seller.isEmpty()) throw new NotFoundException("No hay vendedor con ese ID.");

        Optional<Seller> sellerToRemove = buyer.get().getFollowing().stream()
                .filter(s -> s.getId() == idSeller)
                .findFirst();

        if (sellerToRemove.isPresent()) {
            throw new BadRequestException("No puedes seguir un vendedor que ya sigues.");
        }
        
        buyer.get().addFollowingSeller(seller.get());

        return new FollowDto(idSeller, seller.get().getName());
    }

    @Override
    public FollowersCountDto countSellerFollowers(int userId) {
        //vars
        Optional<Seller> seller = sellerRepository.findById(userId);
        List<Seller> debug = List.of();

        //validate: el usuario obtenido existe y es vendedor
        if(seller.isEmpty()) throw new NotFoundException("El usuario no existe");
        //validacion comentada dado repeticion de id entre compradores y vendedores
        //if(buyerRepository.findById(userId).isPresent()) throw new BadRequestException("El id ingresado debe ser de un vendedor: se obtuvo comprador");

        //process: obtener la lista de seguidos de todos los usuarios, filtrar los que coincidan con el id, hacer el recuento.
        long followersCount = buyerRepository.findAll().stream()
                .filter(comprador -> comprador.getFollowing().stream()
                        .filter(vendedor -> ((Integer) vendedor.getId()).equals(userId)).isParallel()).toList().size();

        //Otra forma
        List<Seller> sellersList =  buyerRepository.findAll().stream().map(Buyer::getFollowing).findAny().orElse(null);
        long otherCount = sellersList.stream().filter(comprador -> comprador.getName().equals(seller.get().getName())).count();
        //return
        return new FollowersCountDto(userId, seller.get().getName(), followersCount);
    }
}
