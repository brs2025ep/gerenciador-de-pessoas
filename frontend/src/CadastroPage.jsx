import React, { useState, useEffect } from 'react';
import { RiResetLeftFill } from 'react-icons/ri';
import { FaSearch } from 'react-icons/fa';

const initialPessoaState = {
  id: null,
  nome: '',
  nascimento: '',
  cpf: '',
  email: '',
  cep: '',
  rua: '',
  numero: '',
  estado: '',
  cidade: '',
};

const CadastroPage = (
  { pessoaToEdit, onSubmit }
) => {
  const [formData, setFormData] = useState(initialPessoaState);
  const [errors, setErrors] = useState({});
  const [showCepSearchIcon, setShowCepSearchIcon] = useState(false);


  useEffect(() => {
    if (pessoaToEdit) {
      setFormData({
        id: pessoaToEdit.id || null,
        nome: pessoaToEdit.nome || '',
        nascimento: pessoaToEdit.nascimento || '',
        cpf: pessoaToEdit.cpf || '',
        email: pessoaToEdit.email || '',
        cep: pessoaToEdit.cep || '',
        rua: pessoaToEdit.rua || '',
        numero: pessoaToEdit.numero || '',
        cidade: pessoaToEdit.cidade || '',
        estado: pessoaToEdit.estado || '',
      });
    } else {
      setFormData(initialPessoaState);
    }
  }, [pessoaToEdit]);

  const displayCepSearchIcon = () => {
    setShowCepSearchIcon(true);
  };

  const hideCepSearchIcon = () => {
    setShowCepSearchIcon(false);
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.nome.trim()) newErrors.nome = 'Nome é obrigatório';

    if (formData.nascimento.length >= 10) {
      nascimentoDateValidation(newErrors);
    }

    if (formData.cpf.trim()) {

      if (formData.cpf.length < 10) {
        newErrors.cpf = 'CPF inválido';
      } 

    }

    if (formData.email.trim()) {
      if (!/\S+@\S+\.\S+/.test(formData.email)) {
        newErrors.email = 'Email é inválido';
      }
    }

    return newErrors;
  };

  const nascimentoDateValidation = (newErrors) => {
    console.log("Teste");
    const [day, month, year] = formData.nascimento.split('/').map(Number);

    const birthDate = new Date(year, month - 1, day);
    const currentDate = new Date();

    birthDate.setHours(0, 0, 0, 0);
    currentDate.setHours(0, 0, 0, 0);

    const isValidDate =
      birthDate.getFullYear() === year &&
      birthDate.getMonth() === month - 1 &&
      birthDate.getDate() === day;

    if (!isValidDate) {
      console.log("Invalid");
      newErrors.nascimento = 'Data de nascimento inválida';
    } else if (birthDate > currentDate) {
      console.log("Valid");
      newErrors.nascimento = 'Data de nascimento não pode ser no futuro';
    }
  }

  const resetCadastroForm = () => {
    setFormData(initialPessoaState);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      console.log('Form Data Submitted.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    let formattedValue = value;

    if (name === 'nome') {
      formattedValue = value
        .split(' ') // Split the string into an array of words
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()) // Capitalize first letter, lowercase rest
        .join(' '); // Join the words back into a string
    }

    if (name === 'nascimento') {
      // Remove all non-digit characters first
      formattedValue = value.replace(/\D/g, '');

      // Apply formatting
      if (formattedValue.length > 2) {
        formattedValue = formattedValue.substring(0, 2) + '/' + formattedValue.substring(2);
      }
      if (formattedValue.length > 5) {
        formattedValue = formattedValue.substring(0, 5) + '/' + formattedValue.substring(5);
      }

      // Limit to 10 characters (dd/MM/YYYY)
      if (formattedValue.length > 10) {
        formattedValue = formattedValue.substring(0, 10);
      }
    }

    if (name === 'cep') { // New logic for CEP field
      // Remove all non-digit characters
      formattedValue = value.replace(/\D/g, '');

      // Limit to 8 digits
      if (formattedValue.length > 8) {
        formattedValue = formattedValue.substring(0, 8);
      }

      // Apply CEP format (00000-000) only if 8 digits are present
      if (formattedValue.length === 8) {
        formattedValue = formattedValue.replace(/(\d{5})(\d{3})/, '$1-$2');
        displayCepSearchIcon();
      } else {
        hideCepSearchIcon();
      }
    }

    if (name === 'cpf') {
      // 1. Remove any non-digit characters
      const digitsOnly = value.replace(/\D/g, '');

      // 2. Limit to 11 digits
      const limitedDigits = digitsOnly.substring(0, 11);

      // 3. Apply Brazilian CPF format 000.000.000-00
      if (limitedDigits.length > 0) {
        formattedValue = limitedDigits.replace(
          /^(\d{3})(\d{3})(\d{3})(\d{2})$/,
          '$1.$2.$3-$4'
        );
      } else {
        formattedValue = '';
      }
    }

    setFormData(prevFormData => ({
      ...prevFormData,
      [name]: formattedValue
    }));
    // Clear the error for the current field as the user types
    if (errors[name]) {
      setErrors(prevErrors => {
        const newErrors = { ...prevErrors };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  return (
    <div>
      <div className="container area-form">
        <h1>{formData.id ? 'Editar Pessoa' : 'Cadastrar Pessoa'}</h1>
        <hr className="solid"></hr>
        <form onSubmit={handleSubmit}>
          <div className="form-row-group">
            <label className="block font-medium">Nome</label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              placeholder="Digite o nome. Exemplo: Gonçalves Dias.."
              onChange={handleChange}
              required />
            {errors.nome && <p className="text-red-500 text-sm">{errors.nome}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">Data nascimento</label>
            <input
              type="text"
              name="nascimento"
              value={formData.nascimento}
              placeholder="Digite a data de nascimento no formato dia/mês/ano. Exemplo: 25/12/1900.."
              onChange={handleChange}
            />
            {errors.nascimento && <p className="text-red-500 text-sm">{errors.nascimento}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">CPF</label>
            <input
              type="text"
              name="cpf"
              value={formData.cpf}
              placeholder="Digite os números do CPF da pessoa..."
              onChange={handleChange}
            />
            {errors.cpf && <p className="text-red-500 text-sm">{errors.cpf}</p>}
          </div>
          <div className="form-row-group">
            <label className="block font-medium">E-mail</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              placeholder="Digite o Email da pessoa..."
              onChange={handleChange}
            />
            {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
          </div>
          <fieldset>
            <legend>Endereço</legend>
            <div className="form-row-group">
              <label htmlFor="cep">Cep</label>
              <div className="relative">
                <input
                  type="text"
                  name="cep"
                  value={formData.cep}
                  placeholder="Digite o CEP da Pessoa.."
                  onChange={handleChange} />
                {showCepSearchIcon && (
                  <div
                    className="absolute inset-y-0 right-0 pr-3 flex items-center cursor-pointer"
                    onClick={() => alert('Ícone de Busca foi acionado: ' + formData.cep)}
                  >
                    <FaSearch /> Buscando
                  </div>
                )}
              </div>
              {errors.cep && <p className="text-red-500 text-sm">{errors.cep}</p>}
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="rua">Rua</label>
                <input
                  type="text"
                  name="rua"
                  value={formData.rua}
                  placeholder="Digita a Rua da Pessoa.."
                  onChange={handleChange} />
                {errors.rua && <p className="text-red-500 text-sm">{errors.rua}</p>}
              </div>
              <div className="form-row-group">

                <label htmlFor="numero">Número</label>
                <input
                  type="number"
                  name="numero"
                  value={formData.numero}
                  placeholder="Digite o número da Residência da Pessoa.."
                  onChange={handleChange} />
                {errors.numero && <p className="text-red-500 text-sm">{errors.numero}</p>}
              </div>
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="cidade">Cidade</label>
                <input
                  type="text"
                  name="cidade"
                  value={formData.cidade}
                  placeholder="Digite o nome da Cidade da Pessoa..."
                  onChange={handleChange} />
                {errors.cidade && <p className="text-red-500 text-sm">{errors.cidade}</p>}
              </div>
              <div className="form-row-group">
                <label htmlFor="estado">Estado</label>
                <input
                  type="text"
                  name="estado"
                  value={formData.estado}
                  placeholder="Digite o nome do Estado da Pessoa..."
                  onChange={handleChange} />
                {errors.estado && <p className="text-red-500 text-sm">{errors.estado}</p>}
              </div>
            </div>
          </fieldset>
          <button
            type="submit"
          >
            {formData.id ? 'Salvar Mudanças' : 'Salvar'}
          </button>


          {formData.id && (
            <> {/* Fragmento para agrupar o botão e o separador */}
              Cancelar<RiResetLeftFill
                className="action-icon"
                onClick={() => resetCadastroForm()}
                title="Cancelar a edição"
              />
            </>
          )}

        </form>
      </div>
    </div>

  );
};

export default CadastroPage;